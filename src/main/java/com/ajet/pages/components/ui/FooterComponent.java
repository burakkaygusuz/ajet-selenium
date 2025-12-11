package com.ajet.pages.components.ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.ajet.pages.components.BaseComponent;

import org.openqa.selenium.support.FindAll;

public class FooterComponent extends BaseComponent {

    @FindAll({
            @FindBy(css = "a[href*='feedback-form']"),
            @FindBy(css = "a[href*='iletisim-formu']")
    })
    private WebElement feedbackFormLink;

    @FindAll({
            @FindBy(css = "a[href*='privacy-statement']"),
            @FindBy(css = "a[href*='gizlilik-bildirimi']")
    })
    private WebElement privacyPolicyLink;

    public FooterComponent(WebDriver driver) {
        super(driver);
    }

    public void clickFeedbackForm() {
        feedbackFormLink.click();
    }

    public void clickPrivacyPolicy() {
        privacyPolicyLink.click();
    }
}
