package com.ajet.tests;

import com.ajet.pages.HomePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Homepage Destination Selection Tests")
class HomePageDestinationTest extends BaseTest {

    @Test
    @DisplayName("Select 'Ankara' from destination dropdown")
    void testSelectDestination() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();
        homePage.selectDestination("Ankara");
    }
}
