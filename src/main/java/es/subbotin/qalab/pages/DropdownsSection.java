package es.subbotin.qalab.pages;

import es.subbotin.qalab.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page object for the Dropdowns section (#dropdowns).
 * Covers single-select and multi-select using the Selenium Select class.
 */
public class DropdownsSection {

    /** Explicit wait configured from config.properties. */
    private final WebDriverWait wait;

    /** Single-select country dropdown. */
    @FindBy(css = "#dropdowns select:not([multiple])")
    private WebElement singleSelect;

    /** Multi-select skills dropdown. */
    @FindBy(css = "#dropdowns select[multiple]")
    private WebElement multiSelect;

    /**
     * Initialises section with driver and Page Factory.
     *
     * @param driver WebDriver instance from BaseTest
     */
    public DropdownsSection(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Selects a single option by its value attribute.
     *
     * @param value option value to select
     */
    public void selectByValue(String value) {
        wait.until(ExpectedConditions.visibilityOf(singleSelect));
        new Select(singleSelect).selectByValue(value);
    }

    /**
     * Selects a single option by visible text.
     *
     * @param text visible text of the option
     */
    public void selectByText(String text) {
        wait.until(ExpectedConditions.visibilityOf(singleSelect));
        new Select(singleSelect).selectByVisibleText(text);
    }

    /**
     * Returns the currently selected option text in the single-select.
     *
     * @return selected option visible text
     */
    public String getSelectedText() {
        wait.until(ExpectedConditions.visibilityOf(singleSelect));
        return new Select(singleSelect).getFirstSelectedOption().getText();
    }

    /**
     * Selects a multi-select option by value. Does not deselect others.
     *
     * @param value option value to select
     */
    public void multiSelectByValue(String value) {
        wait.until(ExpectedConditions.visibilityOf(multiSelect));
        new Select(multiSelect).selectByValue(value);
    }

    /**
     * Deselects all options in the multi-select, then selects the given values.
     *
     * @param values array of option values to select
     */
    public void multiSelectByValues(String... values) {
        wait.until(ExpectedConditions.visibilityOf(multiSelect));
        Select select = new Select(multiSelect);
        select.deselectAll();
        for (String value : values) {
            select.selectByValue(value);
        }
    }

    /**
     * Deselects all options in the multi-select, then selects by visible text.
     * Use this when option value attributes are unknown.
     *
     * @param texts array of visible text labels to select
     */
    public void multiSelectByTexts(String... texts) {
        wait.until(ExpectedConditions.visibilityOf(multiSelect));
        Select select = new Select(multiSelect);
        select.deselectAll();
        for (String text : texts) {
            select.selectByVisibleText(text);
        }
    }

    /**
     * Returns all selected options in the multi-select for assertion in test.
     *
     * @return list of selected WebElements
     */
    public List<WebElement> getMultiSelectedOptions() {
        wait.until(ExpectedConditions.visibilityOf(multiSelect));
        return new Select(multiSelect).getAllSelectedOptions();
    }

    /**
     * Returns the single-select element for assertion in test.
     *
     * @return single-select WebElement
     */
    public WebElement getSingleSelectElement() {
        return singleSelect;
    }
}
