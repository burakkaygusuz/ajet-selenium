package com.ajet.pages.components.ui;

import com.ajet.pages.components.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CitiesBlockComponent extends BaseComponent {

    @FindBy(css = ".cities-block")
    private WebElement citiesBlockRoot;

    @FindBy(css = ".cities-block .region-btn")
    private List<WebElement> tabs;

    @FindBy(css = ".cities-block .country-list, .cities-block .city-list")
    private List<WebElement> destinationLists;

    @FindBy(css = "button.btn-show-flights")
    private List<WebElement> showFlightsButtons;

    public CitiesBlockComponent(WebDriver driver) {
        super(driver);
    }

    public void selectTab(String tabName) {
        ensureVisible();

        wait.until(ExpectedConditions.visibilityOfAllElements(tabs));

        WebElement tab = tabs.stream()
                .filter(t -> t.getText().trim().equalsIgnoreCase(tabName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Tab not found: " + tabName));

        wait.until(ExpectedConditions.elementToBeClickable(tab));
        tab.click();

        // Wait for results to appear (can be country lists or city lists)
        wait.until(ExpectedConditions.visibilityOfAllElements(destinationLists));
    }

    public List<String> getCountries() {
        wait.until(ExpectedConditions.visibilityOfAllElements(destinationLists));
        return destinationLists.stream()
                .filter(el -> el.getAttribute("class").contains("country-list"))
                .map(col -> col.findElement(By.cssSelector(".country-label")).getText().trim())
                .collect(Collectors.toList());
    }

    public List<String> getCitiesInCountry(String countryName) {
        wait.until(ExpectedConditions.visibilityOfAllElements(destinationLists));
        
        return destinationLists.stream()
                .filter(el -> {
                    if (el.getAttribute("class").contains("country-list")) {
                        try {
                            return el.findElement(By.cssSelector(".country-label")).getText().trim().equalsIgnoreCase(countryName);
                        } catch (NoSuchElementException e) {
                            return false;
                        }
                    }
                    return false;
                })
                .findFirst()
                .map(col -> col.findElements(By.tagName("a")).stream()
                        .map(WebElement::getText)
                        .map(String::trim)
                        .collect(Collectors.toList()))
                .orElse(java.util.Collections.emptyList());
    }

    public void clickCity(String cityName) {
        wait.until(ExpectedConditions.visibilityOfAllElements(destinationLists));
        WebElement cityLink = destinationLists.stream()
                .flatMap(el -> el.findElements(By.tagName("a")).stream())
                .filter(link -> link.getText().trim().equalsIgnoreCase(cityName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("City not found: " + cityName));

        scrollTo(cityLink);
        wait.until(ExpectedConditions.elementToBeClickable(cityLink));
        cityLink.click();
    }

    private void ensureVisible() {
        By rootLocator = By.cssSelector(".cities-block");
        
        // Try finding it with incremental scrolling to trigger lazy loading
        for (int i = 0; i < 5; i++) {
            try {
                // Use a short wait to see if the element appeared after scrolling
                new WebDriverWait(driver, Duration.ofMillis(500))
                    .until(ExpectedConditions.visibilityOfElementLocated(rootLocator));
                scrollTo(driver.findElement(rootLocator));
                return; // Found and visible
            } catch (TimeoutException e) {
                // Scroll down to trigger loading
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, window.innerHeight / 2);");
            }
        }

        // If main block not visible, check for the fallback toggle button
        showFlightsButtons.stream()
            .filter(WebElement::isDisplayed)
            .findFirst()
            .ifPresentOrElse(btn -> {
                scrollTo(btn);
                wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
                // Final validation after clicking
                try {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(rootLocator));
                    scrollTo(driver.findElement(rootLocator));
                } catch (TimeoutException e) {
                    throw new NoSuchElementException("Cities Block (.cities-block) still not visible after clicking toggle button.", e);
                }
            }, () -> {
                // No displayed button found
                throw new NoSuchElementException("Cities Block (.cities-block) not visible after scrolling and no toggle button is displayed.");
            });
    }

    private void scrollTo(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
    }
}
