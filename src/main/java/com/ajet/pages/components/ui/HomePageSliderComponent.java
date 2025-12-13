package com.ajet.pages.components.ui;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ajet.pages.components.BaseComponent;

public class HomePageSliderComponent extends BaseComponent {

    @FindBy(css = ".home-page-slider")
    private WebElement sliderContainer;

    @FindBy(css = ".home-page-slider .swiper-slide:not(.swiper-slide-duplicate)")
    private List<WebElement> slides;

    @FindBy(css = ".home-page-slider .swiper-slide-active")
    private WebElement activeSlide;

    @FindBy(css = ".home-page-slider .swp-prev")
    private WebElement prevButton;

    @FindBy(css = ".home-page-slider .swp-next")
    private WebElement nextButton;

    @FindBy(css = ".home-page-slider .swp-paginate span")
    private List<WebElement> paginationDots;

    public HomePageSliderComponent(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            return sliderContainer.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickNext() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }

    public void clickPrevious() {
        wait.until(ExpectedConditions.elementToBeClickable(prevButton)).click();
    }

    public int getSlideCount() {
        return slides.size();
    }

    public String getActiveSlideLink() {
        try {
            WebElement link = activeSlide.findElement(By.tagName("a"));
            return link.getAttribute("href");
        } catch (Exception e) {
            return null;
        }
    }

    public void clickActiveSlide() {
        wait.until(ExpectedConditions.elementToBeClickable(activeSlide)).click();
    }

    public int getActiveSlideIndex() {
        for (int i = 0; i < paginationDots.size(); i++) {
            if (paginationDots.get(i).getAttribute("class").contains("active")) {
                return i;
            }
        }
        return -1;
    }

    public void navigateSlider(HomePageSlider action) {
        Actions actions = new Actions(driver);
        switch (action) {
            case NEXT -> clickNext();
            case PREVIOUS -> clickPrevious();
            case FIRST -> {
                int currentActiveIndex = getActiveSlideIndex();
                if (currentActiveIndex > 0) {
                    for (int i = 0; i < currentActiveIndex; i++) {
                        clickPrevious();
                        actions.pause(Duration.ofSeconds(1)).perform();
                    }
                }
            }
            case LAST -> {
                int currentActiveIndex = getActiveSlideIndex();
                int totalSlides = getSlideCount();
                if (currentActiveIndex != totalSlides - 1) {
                    for (int i = currentActiveIndex; i < totalSlides - 1; i++) {
                        clickNext();
                        actions.pause(Duration.ofSeconds(1)).perform();
                    }
                }
            }
            default -> throw new IllegalArgumentException("Unsupported slider action: " + action);
        }
    }

    public boolean isActionButtonDisabled(HomePageSlider action) {
        return switch (action) {
            case NEXT -> {
                wait.until(ExpectedConditions.visibilityOf(nextButton));
                yield !nextButton.isEnabled();
            }
            case PREVIOUS -> {
                wait.until(ExpectedConditions.visibilityOf(prevButton));
                yield !prevButton.isEnabled();
            }
            case FIRST, LAST -> throw new IllegalArgumentException(
                    "isActionButtonDisabled is only applicable for NEXT and PREVIOUS actions, not " + action);
        };
    }

    public boolean verifyAutoAdvanceToLastSlide(long timeoutSeconds) {
        int lastIndex = getSlideCount() - 1;
        WebDriverWait autoAdvanceWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        try {
            autoAdvanceWait.until(d -> getActiveSlideIndex() == lastIndex);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}



