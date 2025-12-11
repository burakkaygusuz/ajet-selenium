package com.ajet.pages.components.layout;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.ajet.pages.components.BaseComponent;
import java.time.Year;
import java.util.List;
import java.util.function.Supplier;

public class DatePickerComponent extends BaseComponent {

    private static final By DATEPICKER_PANEL = By.cssSelector(".dp__menu");
    private static final By AVAILABLE_DAYS = By.cssSelector(".dp__cell_inner:not(.dp--past):not(.dp__cell_disabled)");
    private static final By MONTH_SELECT_BUTTON = By.cssSelector(".dp__month_year_select:nth-child(1)");
    private static final By YEAR_SELECT_BUTTON = By.cssSelector(".dp__month_year_select:nth-child(2)");
    private static final By OVERLAY_PANEL = By.cssSelector(".dp__overlay");
    private static final By OVERLAY_CELLS = By.cssSelector(".dp__overlay_cell");

    @FindBy(css = "span[aria-label='Previous Month']")
    private WebElement previousMonthButton;

    @FindBy(css = "span[aria-label='Next Month']")
    private WebElement nextMonthButton;

    public DatePickerComponent(WebDriver driver) {
        super(driver);
    }

    public boolean isPickerVisible() {
        try {
            return driver.findElement(DATEPICKER_PANEL).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    private <T> T retryOnStale(Supplier<T> action, int maxRetries) {
        for (int i = 0; i < maxRetries; i++) {
            try {
                return action.get();
            } catch (StaleElementReferenceException e) {
                if (i == maxRetries - 1) {
                    throw e;
                }
            }
        }
        throw new IllegalStateException("Should not reach here");
    }

    public void selectDay(int day) {
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Day must be between 1 and 31, got: " + day);
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(DATEPICKER_PANEL));

        retryOnStale(() -> {
            List<WebElement> days = driver.findElements(AVAILABLE_DAYS);

            WebElement dayElement = days.stream()
                    .filter(e -> {
                        try {
                            return e.getText().trim().equals(String.valueOf(day));
                        } catch (StaleElementReferenceException ex) {
                            return false;
                        }
                    })
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(
                            "Day " + day + " not found in calendar. It may be in the past or another month."));

            wait.until(ExpectedConditions.elementToBeClickable(dayElement));
            dayElement.click();

            return null;
        }, 3);
    }

    public void selectMonth(String monthName) {
        if (monthName == null || monthName.trim().isEmpty()) {
            throw new IllegalArgumentException("Month name cannot be null or empty");
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(DATEPICKER_PANEL));

        retryOnStale(() -> {
            WebElement monthBtn = driver.findElement(MONTH_SELECT_BUTTON);
            wait.until(ExpectedConditions.elementToBeClickable(monthBtn));
            monthBtn.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(OVERLAY_PANEL));

            List<WebElement> cells = driver.findElements(OVERLAY_CELLS);
            WebElement monthCell = cells.stream()
                    .filter(e -> {
                        try {
                            return e.getText().trim().equalsIgnoreCase(monthName.trim());
                        } catch (StaleElementReferenceException ex) {
                            return false;
                        }
                    })
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(
                            "Month '" + monthName + "' not found in overlay panel"));

            wait.until(ExpectedConditions.elementToBeClickable(monthCell));
            monthCell.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(OVERLAY_PANEL));

            return null;
        }, 3);
    }

    public void selectYear(int year) {
        int currentYear = Year.now().getValue();
        if (year < currentYear || year > 2050) {
            throw new IllegalArgumentException(
                    "Year must be between " + currentYear + " and 2050, got: " + year);
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(DATEPICKER_PANEL));

        retryOnStale(() -> {
            WebElement yearBtn = driver.findElement(YEAR_SELECT_BUTTON);
            wait.until(ExpectedConditions.elementToBeClickable(yearBtn));
            yearBtn.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(OVERLAY_PANEL));

            String yearStr = String.valueOf(year);
            List<WebElement> cells = driver.findElements(OVERLAY_CELLS);
            WebElement yearCell = cells.stream()
                    .filter(e -> {
                        try {
                            return e.getText().trim().equals(yearStr);
                        } catch (StaleElementReferenceException ex) {
                            return false;
                        }
                    })
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(
                            "Year " + year + " not found in overlay panel"));

            wait.until(ExpectedConditions.elementToBeClickable(yearCell));
            yearCell.click();

            wait.until(ExpectedConditions.invisibilityOfElementLocated(OVERLAY_PANEL));

            return null;
        }, 3);
    }

    public void goToNextMonth() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(DATEPICKER_PANEL));

        String currentMonth = getCurrentMonth();

        wait.until(ExpectedConditions.elementToBeClickable(nextMonthButton));
        nextMonthButton.click();

        wait.until(driver -> {
            try {
                String newMonth = driver.findElement(MONTH_SELECT_BUTTON).getText().trim();
                return !newMonth.equals(currentMonth);
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        });
    }

    public void goToPreviousMonth() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(DATEPICKER_PANEL));

        String currentMonth = getCurrentMonth();

        wait.until(ExpectedConditions.elementToBeClickable(previousMonthButton));
        previousMonthButton.click();

        wait.until(driver -> {
            try {
                String newMonth = driver.findElement(MONTH_SELECT_BUTTON).getText().trim();
                return !newMonth.equals(currentMonth);
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        });
    }

    public void selectDate(int year, String monthName, int day) {
        selectYear(year);
        selectMonth(monthName);
        selectDay(day);
    }

    public String getCurrentMonth() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(DATEPICKER_PANEL));
        return retryOnStale(() -> {
            WebElement monthBtn = driver.findElement(MONTH_SELECT_BUTTON);
            return monthBtn.getText().trim();
        }, 3);
    }

    public String getCurrentYear() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(DATEPICKER_PANEL));
        return retryOnStale(() -> {
            WebElement yearBtn = driver.findElement(YEAR_SELECT_BUTTON);
            return yearBtn.getText().trim();
        }, 3);
    }
}
