package es.subbotin.qalab.pages;

import es.subbotin.qalab.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page object for the IFrames section (#iframes).
 * Covers switchTo().frame() and switchTo().defaultContent() pattern.
 * Does not use Page Factory — iframe switching requires direct driver calls.
 */
public class IFrameSection {

    /** WebDriver instance — needed for frame switching. */
    private final WebDriver driver;

    /** Explicit wait configured from config.properties. */
    private final WebDriverWait wait;

    /** Locator for the iframe inside the iframes section. */
    private static final By IFRAME_LOCATOR = By.cssSelector("#iframes iframe");

    /**
     * Initialises section with driver. No Page Factory — frame switching is driver-level.
     *
     * @param driver WebDriver instance from BaseTest
     */
    public IFrameSection(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    /**
     * Waits for the iframe to be available and switches into it.
     * Caller must call driver.switchTo().defaultContent() when done,
     * or use getInnerHeadingText() which handles it automatically.
     */
    public void switchToFrame() {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(IFRAME_LOCATOR));
    }

    /**
     * Switches to the iframe, reads the first heading, then returns to the main context.
     *
     * @return heading text from inside the iframe
     */
    public String getInnerHeadingText() {
        switchToFrame();
        WebElement heading = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1, h2, h3"))
        );
        String text = heading.getText();
        driver.switchTo().defaultContent();
        return text;
    }

    /**
     * Returns to the main document context after iframe interaction.
     * Always call this after switchToFrame() to avoid context leaking between tests.
     */
    public void switchToDefault() {
        driver.switchTo().defaultContent();
    }
}
