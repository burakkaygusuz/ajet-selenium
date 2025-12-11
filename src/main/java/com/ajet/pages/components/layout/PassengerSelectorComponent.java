package com.ajet.pages.components.layout;

import com.ajet.pages.components.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Map;

public class PassengerSelectorComponent extends BaseComponent {

    @FindBy(id = "passenger-dropdown")
    private WebElement passengerDropdown;

    @FindBy(css = "ul.p-dropdown-items li")
    private List<WebElement> passengerRows;

    public PassengerSelectorComponent(WebDriver driver) {
        super(driver);
    }

    public void setPassengerCounts(Map<String, Integer> passengerCounts) {
        validatePassengerInputs(passengerCounts);

        try {
            openPassengerDropdown();

            for (Map.Entry<String, Integer> entry : passengerCounts.entrySet()) {
                processSinglePassengerType(entry.getKey(), entry.getValue());
            }

            closePassengerDropdown();

        } catch (TimeoutException e) {
            throw new NoSuchElementException(
                    "Timeout while setting passenger counts. " +
                            "Make sure departure, arrival, and date have been selected first.",
                    e);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(
                    "Failed to set passenger counts: " + e.getMessage(), e);
        }
    }

    private void validatePassengerInputs(Map<String, Integer> passengerCounts) {
        if (passengerCounts == null || passengerCounts.isEmpty()) {
            throw new IllegalArgumentException("Passenger counts map cannot be null or empty");
        }

        int totalPassengers = passengerCounts.values().stream().mapToInt(Integer::intValue).sum();
        if (totalPassengers < 1 || totalPassengers > 9) {
            throw new IllegalArgumentException(
                    "Total passenger count must be between 1 and 9. Got: " + totalPassengers);
        }
    }

    private void openPassengerDropdown() {
        String classes = passengerDropdown.getAttribute("class");
        if (classes != null && classes.contains("disabled")) {
            throw new IllegalStateException(
                    "Passenger dropdown is disabled. Ensure Departure, Arrival, and Date are selected first.");
        }

        wait.until(ExpectedConditions.elementToBeClickable(passengerDropdown));
        passengerDropdown.click();
        wait.until(ExpectedConditions.visibilityOfAllElements(passengerRows));
    }

    private void closePassengerDropdown() {
        driver.findElement(By.cssSelector("body")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("ul.p-dropdown-items")));
    }

    private void processSinglePassengerType(String passengerType, int targetCount) {
        if (targetCount < 0 || targetCount > 9) {
            throw new IllegalArgumentException(
                    "Passenger count for " + passengerType + " must be between 0 and 9.");
        }

        WebElement row = findPassengerRow(passengerType);
        updateCountForRow(row, targetCount);
    }

    private WebElement findPassengerRow(String passengerType) {
        String passengerTypeNormalized = passengerType.trim().toLowerCase();

        for (WebElement row : passengerRows) {
            try {
                WebElement labelElement = row.findElement(By.cssSelector(".label"));
                String rowLabel = labelElement.getText().trim().toLowerCase();

                if (rowLabel.contains(passengerTypeNormalized)) {
                    return row;
                }
            } catch (NoSuchElementException ignored) {
                // Ignore rows that don't match the expected structure
            }
        }
        throw new NoSuchElementException("Could not find passenger type: " + passengerType);
    }

    private void updateCountForRow(WebElement row, int targetCount) {
        final WebElement countElement = row.findElement(By.cssSelector(".counter-value"));
        final WebElement removeButton = row.findElement(By.cssSelector(".counter button:first-of-type"));
        final WebElement addButton = row.findElement(By.cssSelector(".counter button:last-of-type"));

        int currentCount = readPassengerCountFromElement(countElement);
        int difference = targetCount - currentCount;

        if (difference > 0) {
            adjustPassengerCountInternal(addButton, countElement, currentCount, targetCount);
        } else if (difference < 0) {
            adjustPassengerCountInternal(removeButton, countElement, currentCount, targetCount);
        }
    }

    private int readPassengerCountFromElement(WebElement countElement) {
        try {
            String countText = countElement.getText().trim();
            if (!countText.isEmpty()) {
                return Integer.parseInt(countText);
            }
            return 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void adjustPassengerCountInternal(WebElement button, WebElement countElement, int currentCount,
            int targetCount) {
        int step = targetCount > currentCount ? 1 : -1;
        int expected = currentCount;
        while (expected != targetCount) {
            expected += step;
            wait.until(ExpectedConditions.elementToBeClickable(button));
            button.click();
            final int expectedFinal = expected;
            wait.until(d -> {
                try {
                    String txt = countElement.getText().trim();
                    return !txt.isEmpty() && Integer.parseInt(txt) == expectedFinal;
                } catch (NumberFormatException | org.openqa.selenium.StaleElementReferenceException e) {
                    return false;
                }
            });
        }
    }
}
