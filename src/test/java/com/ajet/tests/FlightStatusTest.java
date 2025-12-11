package com.ajet.tests;

import com.ajet.pages.FlightStatusPage;
import com.ajet.pages.HomePage;
import com.ajet.pages.components.ui.FlightSearchTabComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Flight Status Tab Tests")
class FlightStatusTest extends BaseTest {

    @Test
    @DisplayName("Check flight status by flight number")
    void testFlightStatusByFlightNumber() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        FlightSearchTabComponent flightSearch = homePage.getFlightSearchTab();

        FlightStatusPage statusPage = flightSearch.checkFlightStatusByNumber("AJ123");
        assertNotNull(statusPage, "Should return FlightStatusPage");

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after flight status search: " + currentUrl);
        assertNotNull(currentUrl, "URL should not be null");
    }
}
