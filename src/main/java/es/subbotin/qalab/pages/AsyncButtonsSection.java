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
 * Page object for the Async Buttons section (#async-buttons).
 * Covers loading → success/error state transitions without Thread.sleep().
 */
public class AsyncButtonsSection {

    /** Explicit wait configured from config.properties. */
    private final WebDriverWait wait;

    /** Async action button — matched by exact visible text from the live page. */
    @FindBy(xpath = "//button[normalize-space()='Claim Your Prize']")
    private WebElement asyncButton;

    /** Status element that shows loading / success / error feedback text. */
    @FindBy(css = "#async-buttons .status, #async-buttons [class*='status'], #async-buttons [id*='status'], #async-buttons p, #async-buttons span")
    private WebElement statusElement;

    /**
     * Initialises section with driver and Page Factory.
     *
     * @param driver WebDriver instance from BaseTest
     */
    public AsyncButtonsSection(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks the async button to start the operation.
     */
    public void click() {
        wait.until(ExpectedConditions.elementToBeClickable(asyncButton));
        asyncButton.click();
    }

    /**
     * Waits until the status element contains the expected text, then returns it.
     * Uses WebDriverWait — no Thread.sleep().
     *
     * @param expectedText text to wait for (e.g. "Success", "Error")
     * @return actual status text once condition is met
     */
    public String waitForState(String expectedText) {
        wait.until(ExpectedConditions.textToBePresentInElement(statusElement, expectedText));
        return statusElement.getText();
    }

    /**
     * Returns the current status element text without waiting.
     *
     * @return status text
     */
    public String getStatusText() {
        return statusElement.getText();
    }

    /**
     * Returns the status element for assertion in test.
     *
     * @return status WebElement
     */
    public WebElement getStatusElement() {
        wait.until(ExpectedConditions.visibilityOf(statusElement));
        return statusElement;
    }

    /**
     * Returns the async button element for assertion in test.
     *
     * @return async button WebElement
     */
    public WebElement getAsyncButton() {
        return asyncButton;
    }
}
