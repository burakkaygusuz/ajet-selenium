package com.ajet.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ajet.pages.HomePage;
import com.ajet.pages.components.ui.HomePageSlider;
import com.ajet.pages.components.ui.HomePageSliderComponent;

@DisplayName("Home Page Slider Tests")
class HomePageSliderTest extends BaseTest {

    @Test
    @DisplayName("Verify slider visibility, structure and basic navigation")
    void testSliderVisibilityAndStructure() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        HomePageSliderComponent slider = homePage.getHomePageSlider();

        assertTrue(slider.isDisplayed(), "Slider should be displayed on homepage");
        assertTrue(slider.getSlideCount() > 0, "Slider should have at least one slide");

        int initialIndex = slider.getActiveSlideIndex();
        assertTrue(initialIndex >= 0, "Active slide index should be valid");

        slider.clickNext();
    }

    @Test
    @DisplayName("Verify Enum Navigation (FIRST, LAST)")
    void testEnumNavigation() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();
        HomePageSliderComponent slider = homePage.getHomePageSlider();

        slider.navigateSlider(HomePageSlider.LAST);
        int slideCount = slider.getSlideCount();

        assertEquals(slideCount - 1, slider.getActiveSlideIndex(), "Should be at the last slide after navigating to LAST");

        slider.navigateSlider(HomePageSlider.FIRST);
        assertEquals(0, slider.getActiveSlideIndex(), "Should be at the first slide after navigating to FIRST");
    }

    @Test
    @DisplayName("Verify Action Button Disabled State")
    void testActionButtonState() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();
        HomePageSliderComponent slider = homePage.getHomePageSlider();

        boolean nextDisabled = slider.isActionButtonDisabled(HomePageSlider.NEXT);
        boolean prevDisabled = slider.isActionButtonDisabled(HomePageSlider.PREVIOUS);
        
        System.out.println("Next Button Disabled: " + nextDisabled);
        System.out.println("Prev Button Disabled: " + prevDisabled);
        
        assertFalse(nextDisabled, "Next button should likely be enabled in infinite slider");
        assertFalse(prevDisabled, "Prev button should likely be enabled in infinite slider");
    }

    @Test
    @DisplayName("Verify Auto Advance to Last Slide")
    void testAutoAdvance() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();
        HomePageSliderComponent slider = homePage.getHomePageSlider();
        
        int total = slider.getSlideCount();
        if (total > 1) {
            slider.navigateSlider(HomePageSlider.LAST); 
            slider.navigateSlider(HomePageSlider.PREVIOUS);
            
            boolean advanced = slider.verifyAutoAdvanceToLastSlide(15); 

            assertTrue(advanced, "Slider should auto-advance to last slide");
        }
    }
}
