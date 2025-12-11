package com.ajet.tests;

import com.ajet.pages.HomePage;
import com.ajet.pages.components.ui.FlightSearchTabComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("DatePicker Analysis Test")
class DatePickerAnalysisTest extends BaseTest {

    @Test
    @DisplayName("Analyze calendar UX flow with SAW-ESB round trip")
    void testDatePickerUXAnalysis() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        FlightSearchTabComponent flightSearch = homePage.getFlightSearchTab();

        flightSearch.selectRoundTrip()
                .selectDeparture("SAW")
                .selectArrival("GZT")
                .selectDepartureDate(15)
                .selectArrivalDate(20);
    }
}
