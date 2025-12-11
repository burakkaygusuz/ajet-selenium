package com.ajet.pages.components.layout;

import com.ajet.pages.components.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DestinationSelectorComponent extends BaseComponent {

    @FindBy(css = ".from-where .p-dropdown")
    private WebElement destinationDropdown;

    public DestinationSelectorComponent(WebDriver driver) {
        super(driver);
    }

    public void selectDestination(String destinationName) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(destinationDropdown));
            destinationDropdown.click();

            // Wait for list to be visible
            By listLocator = By.cssSelector("ul.p-dropdown-items");
            wait.until(ExpectedConditions.visibilityOfElementLocated(listLocator));

            // Find option containing the text
            // Using XPath for text matching
            By optionLocator = By.xpath("//ul[contains(@class, 'p-dropdown-items')]//li[contains(., '" + destinationName + "')]");
            
            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
            option.click();

            // Wait for list to close
            wait.until(ExpectedConditions.invisibilityOfElementLocated(listLocator));

        } catch (TimeoutException e) {
            throw new NoSuchElementException("Failed to select destination '" + destinationName + "'. Dropdown or option not found.", e);
        }
    }
}
