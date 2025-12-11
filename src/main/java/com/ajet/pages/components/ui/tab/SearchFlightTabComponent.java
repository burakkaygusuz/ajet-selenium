package com.ajet.pages.components.ui.tab;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.ajet.pages.components.BaseComponent;
import com.ajet.pages.components.layout.AirportSelectorComponent;
import com.ajet.pages.components.layout.DatePickerComponent;
import com.ajet.pages.components.layout.PassengerSelectorComponent;

import java.util.Map;

public class SearchFlightTabComponent extends BaseComponent {

    private final DatePickerComponent datePicker;
    private final AirportSelectorComponent airportSelector;
    private final PassengerSelectorComponent passengerSelector;

    // Inputs
    @FindBy(id = "departure-input")
    private WebElement departureInput;

    @FindBy(id = "arrival-input")
    private WebElement arrivalInput;

    @FindBy(id = "dp-input-D")
    private WebElement departureDateInput;

    @FindBy(id = "dp-input-R")
    private WebElement returnDateInput;

    @FindBy(className = "search-button")
    private WebElement searchButton;

    @FindBy(css = "label[for='oneway']")
    private WebElement oneWayRadio;

    @FindBy(css = "label[for='roundtrip']")
    private WebElement roundTripRadio;

    public SearchFlightTabComponent(WebDriver driver) {
        super(driver);
        this.datePicker = new DatePickerComponent(driver);
        this.airportSelector = new AirportSelectorComponent(driver);
        this.passengerSelector = new PassengerSelectorComponent(driver);
    }

    public void selectDeparture(String departureCode) {
        airportSelector.selectAirport(departureInput, departureCode);
    }

    public void selectArrival(String arrivalCode) {
        airportSelector.selectAirport(arrivalInput, arrivalCode);
    }

    public void clickSearchButton() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        searchButton.click();
    }

    public void selectOneWay() {
        wait.until(ExpectedConditions.elementToBeClickable(oneWayRadio));
        oneWayRadio.click();
    }

    public void selectRoundTrip() {
        wait.until(ExpectedConditions.elementToBeClickable(roundTripRadio));
        roundTripRadio.click();
    }

    public void selectDepartureDate(int departureDay) {
        wait.until(ExpectedConditions.elementToBeClickable(departureDateInput));
        departureDateInput.click();
        datePicker.selectDay(departureDay);
    }

    public void selectArrivalDate(int returnDay) {
        try {
            if (!datePicker.isPickerVisible()) {
                wait.until(ExpectedConditions.elementToBeClickable(returnDateInput));
                returnDateInput.click();
            }
        } catch (Exception e) {
            wait.until(ExpectedConditions.elementToBeClickable(returnDateInput));
            returnDateInput.click();
        }
        datePicker.selectDay(returnDay);
    }

    public DatePickerComponent getDatePicker() {
        return datePicker;
    }

    public void setPassengerCounts(Map<String, Integer> passengerCounts) {
        passengerSelector.setPassengerCounts(passengerCounts);
    }

    public void searchFlight() {
        clickSearchButton();
    }
}
