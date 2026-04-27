package es.subbotin.qalab.pages;

import es.subbotin.qalab.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page object for the Checkboxes and Radio Buttons section (#checkboxes).
 * Covers check/uncheck, disabled state, and radio selection.
 */
public class CheckboxesSection {

    /** Explicit wait configured from config.properties. */
    private final WebDriverWait wait;

    /** All checkbox inputs in the section. Index 0-based: options 1–4. */
    @FindBy(css = "#checkboxes input[type='checkbox']")
    private List<WebElement> checkboxes;

    /** All radio inputs in the section: Yes, No, Maybe. */
    @FindBy(css = "#checkboxes input[type='radio']")
    private List<WebElement> radioButtons;

    /**
     * Initialises section with driver and Page Factory.
     *
     * @param driver WebDriver instance from BaseTest
     */
    public CheckboxesSection(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Checks a checkbox by 0-based index if it is not already checked.
     *
     * @param index 0-based index of the checkbox
     */
    public void check(int index) {
        wait.until(ExpectedConditions.visibilityOfAllElements(checkboxes));
        WebElement cb = checkboxes.get(index);
        if (!cb.isSelected()) {
            cb.click();
        }
    }

    /**
     * Unchecks a checkbox by 0-based index if it is currently checked.
     *
     * @param index 0-based index of the checkbox
     */
    public void uncheck(int index) {
        wait.until(ExpectedConditions.visibilityOfAllElements(checkboxes));
        WebElement cb = checkboxes.get(index);
        if (cb.isSelected()) {
            cb.click();
        }
    }

    /**
     * Returns true if the checkbox at the given index is checked.
     *
     * @param index 0-based index of the checkbox
     * @return true when checkbox is selected
     */
    public boolean isChecked(int index) {
        wait.until(ExpectedConditions.visibilityOfAllElements(checkboxes));
        return checkboxes.get(index).isSelected();
    }

    /**
     * Returns true if the checkbox at the given index is disabled.
     *
     * @param index 0-based index of the checkbox
     * @return true when checkbox is not enabled
     */
    public boolean isCheckboxDisabled(int index) {
        wait.until(ExpectedConditions.visibilityOfAllElements(checkboxes));
        return !checkboxes.get(index).isEnabled();
    }

    /**
     * Selects a radio button by 0-based index.
     *
     * @param index 0-based index (0=Yes, 1=No, 2=Maybe)
     */
    public void selectRadio(int index) {
        wait.until(ExpectedConditions.visibilityOfAllElements(radioButtons));
        radioButtons.get(index).click();
    }

    /**
     * Returns true if the radio button at the given index is selected.
     *
     * @param index 0-based index
     * @return true when radio is selected
     */
    public boolean isRadioSelected(int index) {
        wait.until(ExpectedConditions.visibilityOfAllElements(radioButtons));
        return radioButtons.get(index).isSelected();
    }

    /**
     * Returns the full list of checkbox elements for assertion in test.
     *
     * @return list of checkbox WebElements
     */
    public List<WebElement> getCheckboxes() {
        wait.until(ExpectedConditions.visibilityOfAllElements(checkboxes));
        return checkboxes;
    }

    /**
     * Returns the full list of radio button elements for assertion in test.
     *
     * @return list of radio WebElements
     */
    public List<WebElement> getRadioButtons() {
        wait.until(ExpectedConditions.visibilityOfAllElements(radioButtons));
        return radioButtons;
    }
}
