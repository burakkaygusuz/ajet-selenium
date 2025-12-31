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

import java.util.List;
import java.util.stream.Collectors;

public class CitiesBlockComponent extends BaseComponent {

    @FindBy(css = ".cities-block")
    private WebElement citiesBlockRoot;

    @FindBy(css = ".cities-block .region-btn")
    private List<WebElement> tabs;

    @FindBy(css = ".cities-block .country-list")
    private List<WebElement> countryColumns;

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

        wait.until(ExpectedConditions.visibilityOfAllElements(countryColumns));
    }

    public List<String> getCountries() {
        wait.until(ExpectedConditions.visibilityOfAllElements(countryColumns));
        return countryColumns.stream()
                .map(col -> col.findElement(By.cssSelector(".country-label")).getText().trim())
                .collect(Collectors.toList());
    }

    public List<String> getCitiesInCountry(String countryName) {
        wait.until(ExpectedConditions.visibilityOfAllElements(countryColumns));
        WebElement countryColumn = countryColumns.stream()
                .filter(col -> col.findElement(By.cssSelector(".country-label")).getText().trim().equalsIgnoreCase(countryName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Country not found: " + countryName));

        return countryColumn.findElements(By.tagName("a")).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public void clickCity(String cityName) {
        wait.until(ExpectedConditions.visibilityOfAllElements(countryColumns));
        WebElement cityLink = countryColumns.stream()
                .flatMap(col -> col.findElements(By.tagName("a")).stream())
                .filter(link -> link.getText().trim().equalsIgnoreCase(cityName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("City not found: " + cityName));

        scrollTo(cityLink);
        wait.until(ExpectedConditions.elementToBeClickable(cityLink));
        cityLink.click();
    }

    private void ensureVisible() {
        try {
            // First check if already in DOM and visible
            wait.until(ExpectedConditions.visibilityOf(citiesBlockRoot));
            scrollTo(citiesBlockRoot);
        } catch (TimeoutException | NoSuchElementException e) {
            // Try scrolling to bottom to trigger lazy loading
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            try {
                wait.until(ExpectedConditions.visibilityOf(citiesBlockRoot));
                scrollTo(citiesBlockRoot);
            } catch (TimeoutException | NoSuchElementException ex) {
                // Check fallback button (mobile/overlay view)
                if (!showFlightsButtons.isEmpty()) {
                    WebElement btn = showFlightsButtons.get(0);
                    scrollTo(btn);
                    wait.until(ExpectedConditions.elementToBeClickable(btn));
                    btn.click();
                    wait.until(ExpectedConditions.visibilityOf(citiesBlockRoot));
                } else {
                    throw new NoSuchElementException("Cities Block root not found even after scrolling.", ex);
                }
            }
        }
    }

    private void scrollTo(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
    }
}