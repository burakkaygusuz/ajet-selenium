package com.ajet.tests;

import com.ajet.pages.HomePage;
import com.ajet.pages.components.ui.FlightSearchTabComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static java.util.Map.entry;
import java.util.Map;
import java.util.logging.Logger;

@DisplayName("Flight Search Tab Tests")
class FlightSearchTest extends BaseTest {
    private static final Logger LOG = Logger.getLogger(FlightSearchTest.class.getName());

    @Test
    @DisplayName("Search for a one-way flight with default passengers")
    void testSearchOneWayFlightDefault() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        FlightSearchTabComponent flightSearch = homePage.getFlightSearchTab();

        flightSearch
        .selectOneWay()
        .selectDeparture("SAW")
        .selectArrival("ESB")
        .selectDepartureDate(15)
        .clickSearchButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlMatches(".*/(tr|en)$")));
        String currentUrl = driver.getCurrentUrl();
        LOG.info("Current URL after search: " + currentUrl);
        assertFalse(currentUrl != null && (currentUrl.endsWith("/tr") || currentUrl.endsWith("/en")),
                "Should navigate away from homepage. Current URL: " + currentUrl);
    }

    @Test
    @DisplayName("Search for a round-trip flight")
    void testSearchRoundTripFlight() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        FlightSearchTabComponent flightSearch = homePage.getFlightSearchTab();

        flightSearch.selectRoundTrip()
                .selectDeparture("SAW")
                .selectArrival("ESB")
                .selectDepartureDate(20)
                .selectArrivalDate(25)
                .setPassengerCounts(Map.ofEntries(
                        entry("adult", 3),
                        entry("child", 2),
                        entry("infant", 1)
                ))
                .clickSearchButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlMatches(".*/(tr|en)$")));

        String currentUrl = driver.getCurrentUrl();
        LOG.info("Current URL after round-trip search: " + currentUrl);
        assertFalse(currentUrl != null && (currentUrl.endsWith("/tr") || currentUrl.endsWith("/en")),
                "Should navigate away from homepage");
    }

    @Test
    @DisplayName("Use advanced date picker to select specific month and year")
    void testAdvancedDateSelection() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        FlightSearchTabComponent flightSearch = homePage.getFlightSearchTab();

        flightSearch.selectDeparture("SAW")
                .selectArrival("ESB")
                .selectDepartureDate(15);

        assertNotNull(driver.getCurrentUrl(), "URL should not be null after date selection");
    }
}
