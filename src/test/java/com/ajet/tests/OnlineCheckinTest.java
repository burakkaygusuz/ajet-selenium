package com.ajet.tests;

import com.ajet.pages.HomePage;
import com.ajet.pages.components.ui.FlightSearchTabComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Online Check-in Tab Tests")
class OnlineCheckinTest extends BaseTest {

    @Test
    @DisplayName("Navigate to check-in tab and fill form")
    void testOnlineCheckinFormFill() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        FlightSearchTabComponent flightSearch = homePage.getFlightSearchTab();

        flightSearch.clickCheckInTab();
        flightSearch.performCheckIn("ABC123", "TEST");
    }
}
