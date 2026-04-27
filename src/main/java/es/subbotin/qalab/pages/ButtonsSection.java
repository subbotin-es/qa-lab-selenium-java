package es.subbotin.qalab.pages;

import es.subbotin.qalab.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page object for the Buttons section (#buttons).
 * Covers click states and disabled state assertion.
 */
public class ButtonsSection {

    /** Explicit wait configured from config.properties. */
    private final WebDriverWait wait;

    /** Primary-styled button. */
    @FindBy(css = "#buttons .btn-primary:not([disabled])")
    private WebElement primaryButton;

    /** Danger-styled button. */
    @FindBy(css = "#buttons .btn-danger:not([disabled])")
    private WebElement dangerButton;

    /** Disabled button — used for disabled-state assertion. */
    @FindBy(css = "#buttons button[disabled]")
    private WebElement disabledButton;

    /** Submit Order action button. */
    @FindBy(xpath = "//section[@id='buttons']//button[normalize-space()='Submit Order']")
    private WebElement submitOrderButton;

    /** Delete Record action button. */
    @FindBy(xpath = "//section[@id='buttons']//button[normalize-space()='Delete Record']")
    private WebElement deleteRecordButton;

    /** Reset All action button. */
    @FindBy(xpath = "//section[@id='buttons']//button[normalize-space()='Reset All']")
    private WebElement resetAllButton;

    /**
     * Initialises section with driver and Page Factory.
     *
     * @param driver WebDriver instance from BaseTest
     */
    public ButtonsSection(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks the primary button.
     */
    public void clickPrimary() {
        wait.until(ExpectedConditions.elementToBeClickable(primaryButton));
        primaryButton.click();
    }

    /**
     * Clicks the danger button.
     */
    public void clickDanger() {
        wait.until(ExpectedConditions.elementToBeClickable(dangerButton));
        dangerButton.click();
    }

    /**
     * Clicks the Submit Order button.
     */
    public void clickSubmitOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(submitOrderButton));
        submitOrderButton.click();
    }

    /**
     * Clicks the Delete Record button.
     */
    public void clickDeleteRecord() {
        wait.until(ExpectedConditions.elementToBeClickable(deleteRecordButton));
        deleteRecordButton.click();
    }

    /**
     * Clicks the Reset All button.
     */
    public void clickResetAll() {
        wait.until(ExpectedConditions.elementToBeClickable(resetAllButton));
        resetAllButton.click();
    }

    /**
     * Returns the disabled button element for state assertion in test.
     *
     * @return disabled WebElement
     */
    public WebElement getDisabledButton() {
        wait.until(ExpectedConditions.visibilityOf(disabledButton));
        return disabledButton;
    }

    /**
     * Returns true if the disabled button is not enabled.
     *
     * @return true when button is disabled
     */
    public boolean isDisabled() {
        wait.until(ExpectedConditions.visibilityOf(disabledButton));
        return !disabledButton.isEnabled();
    }
}
