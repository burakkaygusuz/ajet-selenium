package com.ajet.tests;

import com.ajet.pages.FlightManagementPage;
import com.ajet.pages.HomePage;
import com.ajet.pages.components.ui.FlightSearchTabComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Happy path tests for the Flight Management tab.
 */
@DisplayName("Flight Management Tab Tests")
class FlightManagementTest extends BaseTest {

    @Test
    @DisplayName("Navigate to flight management tab and fill form")
    void testFlightManagementFormFill() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        FlightSearchTabComponent flightSearch = homePage.getFlightSearchTab();
        flightSearch.clickFlightManagementTab();
        FlightManagementPage managementPage = flightSearch.performManageBooking("XYZ789", "TESTSURNAME");
        assertNotNull(managementPage, "Should return FlightManagementPage");
    }
}
