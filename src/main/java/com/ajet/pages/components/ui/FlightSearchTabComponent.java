package com.ajet.pages.components.ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.ajet.pages.FlightManagementPage;
import com.ajet.pages.FlightStatusPage;
import com.ajet.pages.components.BaseComponent;
import com.ajet.pages.components.ui.tab.CheckInTabComponent;
import com.ajet.pages.components.ui.tab.FlightManagementTabComponent;
import com.ajet.pages.components.ui.tab.FlightStatusTabComponent;
import com.ajet.pages.components.ui.tab.SearchFlightTabComponent;

public class FlightSearchTabComponent extends BaseComponent {

    @FindBy(css = "[role='tablist'] li:nth-of-type(1) [role='tab']")
    private WebElement searchFlightTab;

    @FindBy(css = "[role='tablist'] li:nth-of-type(2) [role='tab']")
    private WebElement checkInTab;

    @FindBy(css = "[role='tablist'] li:nth-of-type(3) [role='tab']")
    private WebElement flightManagementTab;

    @FindBy(css = "[role='tablist'] li:nth-of-type(4) [role='tab']")
    private WebElement flightStatusTab;

    private final SearchFlightTabComponent searchFlightTabComponent;
    private final CheckInTabComponent checkInTabComponent;
    private final FlightManagementTabComponent flightManagementTabComponent;
    private final FlightStatusTabComponent flightStatusTabComponent;

    public FlightSearchTabComponent(WebDriver driver) {
        super(driver);
        this.searchFlightTabComponent = new SearchFlightTabComponent(driver);
        this.checkInTabComponent = new CheckInTabComponent(driver);
        this.flightManagementTabComponent = new FlightManagementTabComponent(driver);
        this.flightStatusTabComponent = new FlightStatusTabComponent(driver);
    }

    public void clickSearchFlightTab() {
        searchFlightTab.click();
    }

    public void clickCheckInTab() {
        checkInTab.click();
    }

    public void clickFlightManagementTab() {
        flightManagementTab.click();
    }

    public void clickFlightStatusTab() {
        flightStatusTab.click();
    }

    public SearchFlightTabComponent getSearchFlightTab() {
        return searchFlightTabComponent;
    }

    public CheckInTabComponent getCheckInTab() {
        return checkInTabComponent;
    }

    public FlightManagementTabComponent getFlightManagementTab() {
        return flightManagementTabComponent;
    }

    public FlightStatusTabComponent getFlightStatusTab() {
        return flightStatusTabComponent;
    }

    public FlightSearchTabComponent selectDeparture(String departureCode) {
        searchFlightTabComponent.selectDeparture(departureCode);
        return this;
    }

    public FlightSearchTabComponent selectArrival(String arrivalCode) {
        searchFlightTabComponent.selectArrival(arrivalCode);
        return this;
    }

    public FlightSearchTabComponent clickSearchButton() {
        searchFlightTabComponent.clickSearchButton();
        return this;
    }

    public FlightSearchTabComponent selectOneWay() {
        searchFlightTabComponent.selectOneWay();
        return this;
    }

    public FlightSearchTabComponent selectRoundTrip() {
        searchFlightTabComponent.selectRoundTrip();
        return this;
    }

    public FlightSearchTabComponent selectDepartureDate(int departureDay) {
        searchFlightTabComponent.selectDepartureDate(departureDay);
        return this;
    }

    public FlightSearchTabComponent selectArrivalDate(int returnDay) {
        searchFlightTabComponent.selectArrivalDate(returnDay);
        return this;
    }

    public FlightSearchTabComponent setPassengerCounts(java.util.Map<String, Integer> passengerCounts) {
        searchFlightTabComponent.setPassengerCounts(passengerCounts);
        return this;
    }

    public FlightSearchTabComponent searchFlight() {
        searchFlightTabComponent.searchFlight();
        return this;
    }

    // Check-in methods
    public FlightSearchTabComponent performCheckIn(String pnr, String surname) {
        clickCheckInTab();
        checkInTabComponent.performCheckIn(pnr, surname);
        return this;
    }

    // Flight Management methods
    public FlightManagementPage performManageBooking(String pnr, String surname) {
        clickFlightManagementTab();
        return flightManagementTabComponent.performManageBooking(pnr, surname);
    }

    // Flight Status methods
    public FlightStatusPage checkFlightStatusByRoute(String from, String to, int date) {
        clickFlightStatusTab();
        return flightStatusTabComponent.checkFlightStatusByRoute(from, to, date);
    }

    public FlightStatusPage checkFlightStatusByNumber(String flightNumber) {
        clickFlightStatusTab();
        return flightStatusTabComponent.checkFlightStatusByNumber(flightNumber);
    }
}
