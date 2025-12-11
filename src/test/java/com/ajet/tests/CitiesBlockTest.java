package com.ajet.tests;

import com.ajet.pages.HomePage;
import com.ajet.pages.components.ui.CitiesBlockComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cities Block Component Tests")
class CitiesBlockTest extends BaseTest {

    @Test
    @DisplayName("Verify tab selection and city click in Cities Block")
    void testCitiesBlockInteraction() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        CitiesBlockComponent citiesBlock = homePage.getCitiesBlock();

        // Select "Europe" tab
        citiesBlock.selectTab("Europe");

        // Verify some European country is present
        assertTrue(citiesBlock.getCountries().contains("Germany"), "Germany should be visible after selecting Europe tab");
        
        // Click on a specific city (e.g., Berlin)
        citiesBlock.clickCity("Berlin");

        // Add assertion to verify navigation or URL change if applicable
        // For now, successful execution without exception implies click worked.
        assertNotNull(driver.getCurrentUrl(), "URL should not be null after clicking a city");
        assertNotEquals("https://ajet.com/en", driver.getCurrentUrl(), "Should navigate away from homepage");
        assertTrue(driver.getCurrentUrl().contains("flights-to-berlin"), "URL should contain 'flights-to-berlin'");
    }

    @Test
    @DisplayName("Verify 'Domestic' tab cities")
    void testDomesticCities() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        CitiesBlockComponent citiesBlock = homePage.getCitiesBlock();

        citiesBlock.selectTab("Domestic");

        // Verify a domestic country/city
        assertTrue(citiesBlock.getCountries().contains("Türkiye"), "Türkiye should be visible after selecting Domestic tab");
        citiesBlock.clickCity("Istanbul"); // Assuming Istanbul is listed under Domestic/Türkiye
        assertNotEquals("https://ajet.com/en", driver.getCurrentUrl(), "Should navigate away from homepage");
        assertTrue(driver.getCurrentUrl().contains("flights-to-istanbul"), "URL should contain 'flights-to-istanbul'");
    }
}
