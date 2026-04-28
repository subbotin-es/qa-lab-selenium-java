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
 * Button: #btn-async-success ("Submit Order") — data-state: idle → loading → success.
 * Status text: #async-status (aria-live region updated after each transition).
 */
public class AsyncButtonsSection {

    /** Explicit wait configured from config.properties. */
    private final WebDriverWait wait;

    /** Submit Order button — tracks state via data-state attribute. */
    @FindBy(id = "btn-async-success")
    private WebElement asyncButton;

    /** Status/result text element updated by the async operation. */
    @FindBy(id = "async-status")
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
     * Waits until the button's data-state attribute equals the expected state,
     * then returns the button text. Uses WebDriverWait — no Thread.sleep().
     * Expected values: "loading", "success", "error".
     *
     * @param expectedState data-state attribute value to wait for (e.g. "success")
     * @return button text once the state is reached (e.g. "Order Confirmed")
     */
    public String waitForState(String expectedState) {
        wait.until(ExpectedConditions.attributeToBe(asyncButton, "data-state", expectedState));
        return asyncButton.getText();
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
     * Returns the async button element for assertion in test.
     *
     * @return async button WebElement
     */
    public WebElement getAsyncButton() {
        wait.until(ExpectedConditions.visibilityOf(asyncButton));
        return asyncButton;
    }
}
