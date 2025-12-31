package com.ajet.pages.components.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

import com.ajet.pages.components.BaseComponent;

public class CitiesBlockComponent extends BaseComponent {

    @FindBy(css = ".cities-block")
    private WebElement citiesBlockRoot;

    @FindBy(css = ".cities-block .region-btn")
    private List<WebElement> tabs;

    @FindBy(css = ".cities-block .country-list, .cities-block .city-list")
    private List<WebElement> destinationLists;

    public CitiesBlockComponent(WebDriver driver) {
        super(driver);
    }

    public void selectTab(String tabName) {
        // Use Actions API to scroll to the component
        new Actions(driver)
                .scrollToElement(citiesBlockRoot)
                .perform();
        
        // Ensure it's centered to avoid overlap with sticky headers
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", citiesBlockRoot);

        wait.until(ExpectedConditions.visibilityOf(citiesBlockRoot));
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

        new Actions(driver)
                .scrollToElement(cityLink)
                .perform();
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", cityLink);
        
        wait.until(ExpectedConditions.elementToBeClickable(cityLink));
        cityLink.click();
    }
}