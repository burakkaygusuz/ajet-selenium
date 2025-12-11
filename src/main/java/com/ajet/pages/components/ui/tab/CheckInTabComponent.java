package com.ajet.pages.components.ui.tab;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.ajet.pages.components.BaseComponent;

public class CheckInTabComponent extends BaseComponent {

    @FindBy(css = "input[id='bookingId'][aria-label*='Ticket']")
    private WebElement checkinPnrInput;

    @FindBy(className = "surname")
    private WebElement checkinSurnameInput;

    @FindBy(css = "button[class*='aj-button primary']")
    private WebElement submitButton;

    public CheckInTabComponent(WebDriver driver) {
        super(driver);
    }

    public CheckInTabComponent enterPnr(String pnr) {
        wait.until(ExpectedConditions.visibilityOf(checkinPnrInput));
        checkinPnrInput.clear();
        checkinPnrInput.sendKeys(pnr);
        return this;
    }

    public CheckInTabComponent enterSurname(String surname) {
        wait.until(ExpectedConditions.visibilityOf(checkinSurnameInput));
        checkinSurnameInput.clear();
        checkinSurnameInput.sendKeys(surname);
        return this;
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
    }

    public void performCheckIn(String pnr, String surname) {
        enterPnr(pnr)
        .enterSurname(surname)
        .clickSubmit();
    }
}
