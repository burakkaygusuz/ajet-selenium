package com.ajet.pages;

import java.util.List;
import java.util.Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.ajet.pages.components.ui.CitiesBlockComponent;
import com.ajet.pages.components.ui.FAQBlockComponent;
import com.ajet.pages.components.ui.FlightSearchTabComponent;
import com.ajet.pages.components.ui.FooterComponent;
import com.ajet.pages.components.ui.HomePageSliderComponent;
import com.ajet.pages.components.ui.NavbarComponent;

public class HomePage extends BasePage {

    private final NavbarComponent navbar;
    private final FlightSearchTabComponent flightSearchTab;
    private final FooterComponent footer;
    private final CitiesBlockComponent citiesBlock;
    private final HomePageSliderComponent homePageSlider;
    private final FAQBlockComponent faqBlock;
    private static final String URL = "https://ajet.com/en";

    public HomePage(WebDriver driver) {
        super(driver);
        this.navbar = new NavbarComponent(driver);
        this.flightSearchTab = new FlightSearchTabComponent(driver);
        this.footer = new FooterComponent(driver);
        this.citiesBlock = new CitiesBlockComponent(driver);
        this.homePageSlider = new HomePageSliderComponent(driver);
        this.faqBlock = new FAQBlockComponent(driver);
    }

    @FindBy(id = "pnl-cookie")
    private WebElement cookieBanner;

    @FindBy(css = ".cookie-bar .primary")
    private WebElement cookieAcceptButton;

    @FindBy(css = ".from-where .p-dropdown")
    private WebElement destinationDropdown;

    @FindBy(css = ".from-where .p-dropdown .p-dropdown-trigger")
    private WebElement destinationTrigger;

    @FindBy(css = "ul.p-dropdown-items")
    private WebElement destinationList;

    @FindBy(css = "ul.p-dropdown-items li")
    private List<WebElement> destinationOptions;

    public void navigateTo() {
        driver.get(URL);
        acceptCookies();
    }

    public void acceptCookies() {
        try {
            if (cookieBanner.isDisplayed()) {
                wait.until(ExpectedConditions.elementToBeClickable(cookieAcceptButton));
                cookieAcceptButton.click();
                wait.until(ExpectedConditions.invisibilityOf(cookieBanner));
            }
        } catch (Exception ignored) {
            // Cookie banner might not be present
        }
    }

    public void selectDestination(String destinationName) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center', inline: 'center'});", destinationDropdown);

            wait.until(ExpectedConditions.visibilityOf(destinationDropdown));

            Actions actions = new Actions(driver);
            actions.moveToElement(Objects.requireNonNull(destinationTrigger)).click().perform();

            wait.until(ExpectedConditions.visibilityOf(destinationList));
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("ul.p-dropdown-items li"), 0));

            WebElement targetOption = destinationOptions.stream()
                    .filter(option -> option.getText().contains(destinationName))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(
                            "Option with text '" + destinationName + "' not found in the list."));

            wait.until(ExpectedConditions.elementToBeClickable(targetOption));

            actions.moveToElement(Objects.requireNonNull(targetOption)).click().perform();

            wait.until(ExpectedConditions.invisibilityOf(destinationList));

        } catch (TimeoutException e) {
            throw new NoSuchElementException(
                    "Failed to select destination '" + destinationName + "'. Dropdown interaction failed.", e);
        }
    }

    public NavbarComponent getNavbar() {
        return navbar;
    }

    public FlightSearchTabComponent getFlightSearchTab() {
        return flightSearchTab;
    }

    public FooterComponent getFooter() {
        return footer;
    }

    public CitiesBlockComponent getCitiesBlock() {
        return citiesBlock;
    }

    public HomePageSliderComponent getHomePageSlider() {
        return homePageSlider;
    }

    public FAQBlockComponent getFAQBlock() {
        return faqBlock;
    }
}
