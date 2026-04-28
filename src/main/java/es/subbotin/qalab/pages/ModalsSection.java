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
 * Page object for the Alerts and Modals section (#alerts).
 * Covers modal open, confirm, cancel, and dismiss.
 */
public class ModalsSection {

    /** Explicit wait configured from config.properties. */
    private final WebDriverWait wait;

    /** Button that opens the modal dialog — matched by exact visible text. */
    @FindBy(xpath = "//button[normalize-space()='Open Modal']")
    private WebElement openModalButton;

    /** Confirm button inside the open modal — matched by visible text. */
    @FindBy(xpath = "//div[contains(@class,'modal')]//button[normalize-space()='Confirm']")
    private WebElement confirmButton;

    /** Cancel button inside the open modal — matched by visible text. */
    @FindBy(xpath = "//div[contains(@class,'modal')]//button[normalize-space()='Cancel']")
    private WebElement cancelButton;

    /** Modal dialog container — matched broadly; Bootstrap may inject it into body. */
    @FindBy(css = ".modal")
    private WebElement modalContainer;

    /**
     * Initialises section with driver and Page Factory.
     *
     * @param driver WebDriver instance from BaseTest
     */
    public ModalsSection(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks the button that opens the modal dialog.
     */
    public void open() {
        wait.until(ExpectedConditions.elementToBeClickable(openModalButton));
        openModalButton.click();
    }

    /**
     * Clicks the Confirm button inside the open modal.
     */
    public void confirm() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
        confirmButton.click();
    }

    /**
     * Clicks the Cancel button inside the open modal.
     */
    public void cancel() {
        wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
        cancelButton.click();
    }

    /**
     * Returns true if the modal container is currently visible.
     *
     * @return true when modal is displayed
     */
    public boolean isModalVisible() {
        try {
            return modalContainer.isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns the modal container element for assertion in test.
     * Waits for visibility before returning — suitable after open().
     *
     * @return modal WebElement
     */
    public WebElement getModalContainer() {
        wait.until(ExpectedConditions.visibilityOf(modalContainer));
        return modalContainer;
    }

    /**
     * Waits until the modal container is no longer visible.
     * Call after confirm() or cancel() to synchronise before assertion.
     */
    public void waitForModalClosed() {
        wait.until(ExpectedConditions.invisibilityOf(modalContainer));
    }
}
