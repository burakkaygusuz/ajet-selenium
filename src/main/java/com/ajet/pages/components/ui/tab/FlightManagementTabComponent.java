package com.ajet.pages.components.ui.tab;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.ajet.pages.FlightManagementPage;
import com.ajet.pages.components.BaseComponent;

public class FlightManagementTabComponent extends BaseComponent {

    @FindAll({
            @FindBy(css = "input[id='bookingId'][aria-label*='PNR']"),
            @FindBy(css = "input[id='bookingId'][aria-label*='Pnr']")
    })
    private WebElement manageBookingPnrInput;

    @FindBy(css = "input[aria-label='Surname']")
    private WebElement manageBookingSurnameInput;

    @FindBy(css = "button[class*='aj-button primary']")
    private WebElement submitButton;

    public FlightManagementTabComponent(WebDriver driver) {
        super(driver);
    }

    public FlightManagementTabComponent enterPnr(String pnr) {
        wait.until(ExpectedConditions.visibilityOf(manageBookingPnrInput));
        manageBookingPnrInput.clear();
        manageBookingPnrInput.sendKeys(pnr);
        return this;
    }

    public FlightManagementTabComponent enterSurname(String surname) {
        wait.until(ExpectedConditions.visibilityOf(manageBookingSurnameInput));
        manageBookingSurnameInput.clear();
        manageBookingSurnameInput.sendKeys(surname);
        return this;
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
    }

    public FlightManagementPage performManageBooking(String pnr, String surname) {
        enterPnr(pnr)
        .enterSurname(surname)
        .clickSubmit();
        return new FlightManagementPage(driver);
    }
}
