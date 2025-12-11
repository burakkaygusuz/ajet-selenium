package com.ajet.pages.components.ui;

import com.ajet.pages.components.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

public class CitiesBlockComponent extends BaseComponent {

    @FindBy(xpath = "//h2[contains(text(), 'All Our Flight Arrivals')]/parent::div")
    private WebElement citiesBlockRoot;

    @FindBy(xpath = "//h2[contains(text(), 'All Our Flight Arrivals')]/following-sibling::div[1]//button[contains(@class, 'tab-button')]")
    private List<WebElement> tabs;

    @FindBy(xpath = "//h2[contains(text(), 'All Our Flight Arrivals')]/following-sibling::div[2]//div[contains(@class, 'country-column')]")
    private List<WebElement> countryColumns;

    public CitiesBlockComponent(WebDriver driver) {
        super(driver);
    }

    public void selectTab(String tabName) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", citiesBlockRoot);
        
        // Wait for at least one tab button to be present before checking visibility of all
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.cities-block .tab-button")));
        wait.until(ExpectedConditions.visibilityOfAllElements(tabs));
        WebElement tab = tabs.stream()
                .filter(t -> t.getText().equalsIgnoreCase(tabName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Tab not found: " + tabName));
        
        wait.until(ExpectedConditions.elementToBeClickable(tab));
        tab.click();
        wait.until(ExpectedConditions.visibilityOfAllElements(countryColumns));
    }

    public List<String> getCountries() {
        wait.until(ExpectedConditions.visibilityOfAllElements(countryColumns)); // Ensure elements are visible after tab click
        return countryColumns.stream()
                .map(col -> col.findElement(By.cssSelector("h3")).getText().trim())
                .collect(Collectors.toList());
    }

    public List<String> getCitiesInCountry(String countryName) {
        wait.until(ExpectedConditions.visibilityOfAllElements(countryColumns));
        WebElement countryColumn = countryColumns.stream()
                .filter(col -> col.findElement(By.cssSelector("h3")).getText().equalsIgnoreCase(countryName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Country not found: " + countryName));
        
        return countryColumn.findElements(By.cssSelector("ul.city-list li a")).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public void clickCity(String cityName) {
        wait.until(ExpectedConditions.visibilityOfAllElements(countryColumns));
        WebElement cityLink = countryColumns.stream()
                .flatMap(col -> col.findElements(By.cssSelector("ul.city-list li a")).stream())
                .filter(link -> link.getText().equalsIgnoreCase(cityName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("City not found: " + cityName));

        wait.until(ExpectedConditions.elementToBeClickable(cityLink));
        cityLink.click();
    }
}
