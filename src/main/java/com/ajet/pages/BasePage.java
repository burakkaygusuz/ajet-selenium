package com.ajet.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Objects;

public class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected static final Duration DEFAULT_WAIT = Duration.ofSeconds(10);

    public BasePage(WebDriver driver) {
        this.driver = Objects.requireNonNull(driver, "WebDriver must not be null");
        this.wait = new WebDriverWait(this.driver, DEFAULT_WAIT);
        PageFactory.initElements(this.driver, this);
    }
}
