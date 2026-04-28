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
 * Page object for the Dynamic Visibility section (#dynamic-visibility).
 * Covers checkbox-triggered panel reveal and hide.
 */
public class DynamicVisibilitySection {

    /** Explicit wait configured from config.properties. */
    private final WebDriverWait wait;

    /** Checkbox that toggles the hidden panel. */
    @FindBy(css = "#dynamic-visibility input[type='checkbox']")
    private WebElement toggleCheckbox;

    /** Panel revealed when checkbox is checked — located by its known text content. */
    @FindBy(xpath = "//*[@id='dynamic-visibility']//*[contains(text(),'Secret panel revealed')]")
    private WebElement secretPanel;

    /**
     * Initialises section with driver and Page Factory.
     *
     * @param driver WebDriver instance from BaseTest
     */
    public DynamicVisibilitySection(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks the toggle checkbox to show or hide the secret panel.
     */
    public void toggleCheckbox() {
        wait.until(ExpectedConditions.elementToBeClickable(toggleCheckbox));
        toggleCheckbox.click();
    }

    /**
     * Checks the toggle checkbox to ensure the panel is visible.
     * No-ops if already checked.
     */
    public void showPanel() {
        wait.until(ExpectedConditions.visibilityOf(toggleCheckbox));
        if (!toggleCheckbox.isSelected()) {
            toggleCheckbox.click();
        }
    }

    /**
     * Unchecks the toggle checkbox to ensure the panel is hidden.
     * No-ops if already unchecked.
     */
    public void hidePanel() {
        wait.until(ExpectedConditions.visibilityOf(toggleCheckbox));
        if (toggleCheckbox.isSelected()) {
            toggleCheckbox.click();
        }
    }

    /**
     * Returns true if the secret panel is currently visible in the DOM.
     *
     * @return true when panel is displayed
     */
    public boolean isPanelVisible() {
        try {
            return secretPanel.isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Waits until the panel is visible and returns it for assertion in test.
     *
     * @return secret panel WebElement
     */
    public WebElement getSecretPanel() {
        wait.until(ExpectedConditions.visibilityOf(secretPanel));
        return secretPanel;
    }

    /**
     * Returns the toggle checkbox element for assertion in test.
     *
     * @return toggle checkbox WebElement
     */
    public WebElement getToggleCheckbox() {
        return toggleCheckbox;
    }
}
