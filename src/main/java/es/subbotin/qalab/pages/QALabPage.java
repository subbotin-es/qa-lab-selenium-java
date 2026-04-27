package es.subbotin.qalab.pages;

import es.subbotin.qalab.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base page object — navigation and shared utilities for all sections.
 */
public class QALabPage {

    /** WebDriver instance for this page. */
    private final WebDriver driver;

    /** Explicit wait configured from config.properties. */
    private final WebDriverWait wait;

    /**
     * Initialises page with driver and Page Factory.
     *
     * @param driver WebDriver instance from BaseTest
     */
    public QALabPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Navigates to the QA Lab target page.
     */
    public void navigate() {
        driver.get(ConfigReader.getBaseUrl() + "/QA-Lab/qa-lab.html");
    }

    /**
     * Scrolls a section into the viewport using its HTML id attribute.
     *
     * @param sectionId the id attribute value (without #)
     */
    public void scrollToSection(String sectionId) {
        WebElement section = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.id(sectionId))
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", section);
    }

    /**
     * Returns the browser tab title.
     *
     * @return page title string
     */
    public String getTitle() {
        return driver.getTitle();
    }
}
