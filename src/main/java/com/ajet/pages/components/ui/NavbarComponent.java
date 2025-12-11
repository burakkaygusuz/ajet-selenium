package com.ajet.pages.components.ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.ajet.pages.components.BaseComponent;

import org.openqa.selenium.support.FindAll;

public class NavbarComponent extends BaseComponent {

    @FindBy(css = "a.home-link")
    private WebElement logo;

    @FindAll({
            @FindBy(css = "a[href*='services']"),
            @FindBy(css = "a[href*='hizmetlerimiz']")
    })
    private WebElement servicesLink;

    @FindAll({
            @FindBy(css = "a[href*='city-guide']"),
            @FindBy(css = "a[href*='sehir-rehberi']")
    })
    private WebElement cityGuideLink;

    @FindAll({
            @FindBy(css = "a[href*='frequently-asked-questions']"),
            @FindBy(css = "a[href*='sikca-sorulan-sorular']")
    })
    private WebElement faqLink;

    @FindAll({
            @FindBy(css = "a[href*='campaigns']"),
            @FindBy(css = "a[href*='kampanyalar']")
    })
    private WebElement campaignsLink;

    @FindBy(css = "header button.header-user-btn")
    private WebElement loginButton;

    public NavbarComponent(WebDriver driver) {
        super(driver);
    }

    public void clickLogo() {
        logo.click();
    }

    public void clickLogin() {
        loginButton.click();
    }

    public void clickServices() {
        servicesLink.click();
    }

    public void clickCityGuide() {
        cityGuideLink.click();
    }

    public void clickFAQ() {
        faqLink.click();
    }

    public void clickCampaigns() {
        campaignsLink.click();
    }
}
