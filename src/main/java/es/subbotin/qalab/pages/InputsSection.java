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
 * Page object for the Input Fields section (#inputs).
 * Covers text, number, date, search, and URL input types.
 */
public class InputsSection {

    /** Explicit wait configured from config.properties. */
    private final WebDriverWait wait;

    /** Standalone text input. */
    @FindBy(css = "#inputs input[type='text']")
    private WebElement textInput;

    /** Standalone number input — scoped to #inputs to avoid collision with forms age field. */
    @FindBy(css = "#inputs input[type='number']")
    private WebElement numberInput;

    /** Date input. */
    @FindBy(css = "#inputs input[type='date']")
    private WebElement dateInput;

    /** Search input. */
    @FindBy(css = "#inputs input[type='search']")
    private WebElement searchInput;

    /** URL input. */
    @FindBy(css = "#inputs input[type='url']")
    private WebElement urlInput;

    /**
     * Initialises section with driver and Page Factory.
     *
     * @param driver WebDriver instance from BaseTest
     */
    public InputsSection(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Clears and types into the text input.
     *
     * @param value text to type
     */
    public void fillText(String value) {
        wait.until(ExpectedConditions.visibilityOf(textInput));
        textInput.clear();
        textInput.sendKeys(value);
    }

    /**
     * Clears and types into the number input.
     *
     * @param value number as string
     */
    public void fillNumber(String value) {
        wait.until(ExpectedConditions.visibilityOf(numberInput));
        numberInput.clear();
        numberInput.sendKeys(value);
    }

    /**
     * Clears and types a date value into the date input.
     * Expected format depends on browser locale — use ISO format (yyyy-MM-dd).
     *
     * @param value date string
     */
    public void fillDate(String value) {
        wait.until(ExpectedConditions.visibilityOf(dateInput));
        dateInput.clear();
        dateInput.sendKeys(value);
    }

    /**
     * Clears and types into the search input.
     *
     * @param value search query
     */
    public void fillSearch(String value) {
        wait.until(ExpectedConditions.visibilityOf(searchInput));
        searchInput.clear();
        searchInput.sendKeys(value);
    }

    /**
     * Clears and types into the URL input.
     *
     * @param value URL string
     */
    public void fillUrl(String value) {
        wait.until(ExpectedConditions.visibilityOf(urlInput));
        urlInput.clear();
        urlInput.sendKeys(value);
    }

    /**
     * Returns the text input element for assertion in test.
     *
     * @return text WebElement
     */
    public WebElement getTextInput() {
        return textInput;
    }

    /**
     * Returns the number input element for assertion in test.
     *
     * @return number WebElement
     */
    public WebElement getNumberInput() {
        return numberInput;
    }

    /**
     * Returns the date input element for assertion in test.
     *
     * @return date WebElement
     */
    public WebElement getDateInput() {
        return dateInput;
    }

    /**
     * Returns the search input element for assertion in test.
     *
     * @return search WebElement
     */
    public WebElement getSearchInput() {
        return searchInput;
    }

    /**
     * Returns the URL input element for assertion in test.
     *
     * @return url WebElement
     */
    public WebElement getUrlInput() {
        return urlInput;
    }
}
