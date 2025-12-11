package com.ajet.pages.components.ui.tab;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.ajet.pages.FlightStatusPage;
import com.ajet.pages.components.BaseComponent;
import com.ajet.pages.components.layout.DatePickerComponent;

import java.util.Objects;

public class FlightStatusTabComponent extends BaseComponent {

    private final DatePickerComponent datePicker;
    private static final String DROPDOWN_LIST = ".el-select-dropdown__list";

    @FindBy(css = "label[for='route']")
    private WebElement statusRouteRadio;

    @FindBy(css = "label[for='flightSearch']")
    private WebElement statusFlightNoRadio;

    @FindBy(name = "flightNumber")
    private WebElement statusFlightNumberInput;

    @FindBy(id = "dp-input-T")
    private WebElement statusDateInput;

    @FindBy(id = "departure-input")
    private WebElement departureInput;

    @FindBy(id = "arrival-input")
    private WebElement arrivalInput;

    @FindBy(css = "button[class*='aj-button primary']")
    private WebElement submitButton;

    public FlightStatusTabComponent(WebDriver driver) {
        super(driver);
        this.datePicker = new DatePickerComponent(driver);
    }

    public void selectRouteOption() {
        wait.until(ExpectedConditions.elementToBeClickable(statusRouteRadio));
        statusRouteRadio.click();
    }

    public void selectFlightNumberOption() {
        wait.until(ExpectedConditions.elementToBeClickable(statusFlightNoRadio));
        statusFlightNoRadio.click();
    }

    public void enterFlightNumber(String flightNumber) {
        wait.until(ExpectedConditions.visibilityOf(statusFlightNumberInput));
        statusFlightNumberInput.clear();
        statusFlightNumberInput.sendKeys(flightNumber);
    }

    public void selectDeparture(String departureCode) {
        selectAirport(departureInput, departureCode);
    }

    public void selectArrival(String arrivalCode) {
        selectAirport(arrivalInput, arrivalCode);
    }

    public void selectDate(int day) {
        wait.until(ExpectedConditions.elementToBeClickable(statusDateInput));
        statusDateInput.click();
        datePicker.selectDay(day);
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
    }

    public DatePickerComponent getDatePicker() {
        return datePicker;
    }

    public FlightStatusPage checkFlightStatusByRoute(String from, String to, int date) {
        selectRouteOption();
        selectDeparture(from);
        selectArrival(to);
        selectDate(date);
        clickSubmit();
        return new FlightStatusPage(driver);
    }

    public FlightStatusPage checkFlightStatusByNumber(String flightNumber) {
        selectFlightNumberOption();
        enterFlightNumber(flightNumber);
        clickSubmit();
        return new FlightStatusPage(driver);
    }

    private void selectAirport(WebElement input, String code) {
        Objects.requireNonNull(input, "input must not be null");
        Objects.requireNonNull(code, "code must not be null");
        wait.until(ExpectedConditions.elementToBeClickable(input));
        input.click();
        input.clear();
        input.sendKeys(code);
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(java.util.Objects.requireNonNull(By.cssSelector(DROPDOWN_LIST))));
        String itemXPath = "//ul[contains(@class, 'el-select-dropdown__list')]//li[contains(., '" + code + "')]";
        wait.until(ExpectedConditions.elementToBeClickable(java.util.Objects.requireNonNull(By.xpath(itemXPath))));
        driver.findElement(By.xpath(itemXPath)).click();
    }
}
