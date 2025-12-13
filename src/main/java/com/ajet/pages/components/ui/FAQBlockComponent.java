package com.ajet.pages.components.ui;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.ajet.pages.components.BaseComponent;

public class FAQBlockComponent extends BaseComponent {

    @FindBy(css = ".faq-block")
    private WebElement container;

    @FindBy(css = ".faq-block .cat-select .p-dropdown")
    private WebElement categoryDropdown;

    @FindBy(css = ".faq-block .cat-select .p-dropdown .p-dropdown-trigger")
    private WebElement categoryDropdownTrigger;
    
    @FindBy(css = ".p-dropdown-panel .p-dropdown-items .p-dropdown-item")
    private List<WebElement> categoryOptions;

    @FindBy(css = ".faq-block .p-accordion .p-accordion-tab")
    private List<WebElement> questionTabs;

    public FAQBlockComponent(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        try {
            return container.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getActiveCategory() {
        return categoryDropdown.getText();
    }

    public void selectCategory(String categoryName) {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", categoryDropdownTrigger);
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".p-dropdown-panel .p-dropdown-items")));
        
        for (WebElement option : categoryOptions) {
            if (option.getText().trim().equals(categoryName)) {
                option.click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".p-dropdown-panel")));
                return;
            }
        }
        throw new RuntimeException("Category '" + categoryName + "' not found in FAQ dropdown");
    }

    public List<String> getQuestions() {
        return questionTabs.stream()
                .map(tab -> tab.findElement(By.cssSelector(".p-accordion-header-text")).getText().trim())
                .collect(Collectors.toList());
    }

    public void expandQuestion(String questionText) {
        for (WebElement tab : questionTabs) {
            WebElement header = tab.findElement(By.cssSelector(".p-accordion-header"));
            String text = header.findElement(By.cssSelector(".p-accordion-header-text")).getText().trim();
            if (text.equals(questionText)) {
                if (!tab.getAttribute("class").contains("p-accordion-tab-active")) {
                    header.click();
                    wait.until(ExpectedConditions.attributeContains(tab, "class", "p-accordion-tab-active"));
                }
                return;
            }
        }
        throw new RuntimeException("Question '" + questionText + "' not found");
    }

    public String getAnswerText(String questionText) {
        for (WebElement tab : questionTabs) {
            String text = tab.findElement(By.cssSelector(".p-accordion-header-text")).getText().trim();
            if (text.equals(questionText)) {
                return tab.findElement(By.cssSelector(".p-accordion-content")).getText().trim();
            }
        }
        throw new RuntimeException("Question '" + questionText + "' not found");
    }

    public List<String> getAllCategories() {
        // Use JS Click to avoid interception
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", categoryDropdownTrigger);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".p-dropdown-panel .p-dropdown-items")));
        
        List<String> categories = categoryOptions.stream()
                .map(WebElement::getText)
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());

        // Close the dropdown using JS click as well
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", categoryDropdownTrigger);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".p-dropdown-panel")));
        
        return categories;
    }

    /**
     * Selects a category based on a CONSTANT_CASE key (e.g. RESERVATION_AND_TICKETING).
     * It fuzzy matches the key words with available options.
     */
    public void selectCategoryByKey(String key) {
        String normalizedKey = key.replace("_", " ").toLowerCase();
        
        // Open dropdown to get options (using JS as per fix)
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", categoryDropdownTrigger);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".p-dropdown-panel .p-dropdown-items")));
        
        WebElement bestMatch = null;
        int maxMatches = 0;

        for (WebElement option : categoryOptions) {
            String optionText = option.getText().toLowerCase();
            int matches = 0;
            for (String word : normalizedKey.split(" ")) {
                if (optionText.contains(word)) {
                    matches++;
                }
            }
            if (matches > maxMatches) {
                maxMatches = matches;
                bestMatch = option;
            }
        }

        if (bestMatch != null) {
            bestMatch.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".p-dropdown-panel")));
        } else {
            // Close if no match found
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", categoryDropdownTrigger);
            throw new RuntimeException("No matching category found for key: " + key);
        }
    }

    /**
     * Expands a question based on a CONSTANT_CASE key (e.g. WHERE_CAN_I_BUY).
     * It fuzzy matches the key words with available questions.
     */
    public void expandQuestionByKey(String key) {
        String normalizedKey = key.replace("_", " ").toLowerCase();
        
        WebElement bestMatchTab = null;
        int maxMatches = 0;

        for (WebElement tab : questionTabs) {
            String questionText = tab.findElement(By.cssSelector(".p-accordion-header-text")).getText().toLowerCase();
            int matches = 0;
            // Filter out common words to improve accuracy if needed, but basic split is a good start
            for (String word : normalizedKey.split(" ")) {
                if (questionText.contains(word)) {
                    matches++;
                }
            }
            if (matches > maxMatches) {
                maxMatches = matches;
                bestMatchTab = tab;
            }
        }

        if (bestMatchTab != null) {
            WebElement header = bestMatchTab.findElement(By.cssSelector(".p-accordion-header"));
            if (!bestMatchTab.getAttribute("class").contains("p-accordion-tab-active")) {
                header.click();
                wait.until(ExpectedConditions.attributeContains(bestMatchTab, "class", "p-accordion-tab-active"));
            }
        } else {
            throw new RuntimeException("No matching question found for key: " + key);
        }
    }
}
