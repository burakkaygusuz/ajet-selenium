package com.ajet.pages.components.layout;

import com.ajet.pages.components.BaseComponent;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Objects;

public class AirportSelectorComponent extends BaseComponent {

    public AirportSelectorComponent(WebDriver driver) {
        super(driver);
    }

    public void selectAirport(WebElement input, String code) {
        Objects.requireNonNull(input, "input must not be null");
        Objects.requireNonNull(code, "code must not be null");

        try {
            input.click();
            input.clear();
            input.sendKeys(code);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul[role='listbox']")));
            wait.until(ExpectedConditions
                    .numberOfElementsToBeMoreThan(By.cssSelector("ul[role='listbox'] [role='option']"), 0));

            List<WebElement> options = driver
                    .findElements(By.cssSelector("ul[role='listbox'] [role='option']"));

            WebElement matchedOption = options.stream()
                    .filter(option -> {
                        try {
                            List<WebElement> codes = option.findElements(By.cssSelector("div.option-code"));
                            return codes.stream().anyMatch(optionCode -> optionCode.getText().equalsIgnoreCase(code));
                        } catch (StaleElementReferenceException e) {
                            return false;
                        }
                    })
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Option not found for code: " + code));

            wait.until(ExpectedConditions.elementToBeClickable(matchedOption));
            matchedOption.click();

        } catch (TimeoutException | IndexOutOfBoundsException e) {
            throw new NoSuchElementException(
                    "Failed to select airport '" + code + "': " + e.getMessage(), e);
        }
    }
}
