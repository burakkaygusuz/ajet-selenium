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
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", categoryDropdownTrigger);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".p-dropdown-panel .p-dropdown-items")));
        
        List<String> categories = categoryOptions.stream()
                .map(WebElement::getText)
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());

        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", categoryDropdownTrigger);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".p-dropdown-panel")));
        
        return categories;
    }
}
