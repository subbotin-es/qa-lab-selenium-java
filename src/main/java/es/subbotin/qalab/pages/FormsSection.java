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
 * Page object for the Registration Form section (#forms).
 * Covers field interaction, validation, and submit.
 */
public class FormsSection {

    /** Explicit wait configured from config.properties. */
    private final WebDriverWait wait;

    /** Full Name text input. */
    @FindBy(css = "input[placeholder='Full Name']")
    private WebElement fullNameInput;

    /** Email input. */
    @FindBy(css = "input[type='email']")
    private WebElement emailInput;

    /** Age number input. */
    @FindBy(css = "#forms input[type='number']")
    private WebElement ageInput;

    /** Phone tel input. */
    @FindBy(css = "input[type='tel']")
    private WebElement phoneInput;

    /** Register submit button. */
    @FindBy(xpath = "//button[normalize-space()='Register']")
    private WebElement registerButton;

    /**
     * Initialises section with driver and Page Factory.
     *
     * @param driver WebDriver instance from BaseTest
     */
    public FormsSection(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Fills all registration form fields.
     *
     * @param fullName name value
     * @param email    email value
     * @param age      age value
     * @param phone    phone value
     */
    public void fillForm(String fullName, String email, int age, String phone) {
        wait.until(ExpectedConditions.visibilityOf(fullNameInput));
        fullNameInput.clear();
        fullNameInput.sendKeys(fullName);
        emailInput.clear();
        emailInput.sendKeys(email);
        ageInput.clear();
        ageInput.sendKeys(String.valueOf(age));
        phoneInput.clear();
        phoneInput.sendKeys(phone);
    }

    /**
     * Submits the registration form.
     */
    public void submit() {
        wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        registerButton.click();
    }

    /**
     * Returns the Register button element for assertion in test.
     *
     * @return Register WebElement
     */
    public WebElement getRegisterButton() {
        return registerButton;
    }

    /**
     * Returns the Full Name input element for assertion in test.
     *
     * @return fullName WebElement
     */
    public WebElement getFullNameInput() {
        return fullNameInput;
    }

    /**
     * Returns the email input element for assertion in test.
     *
     * @return email WebElement
     */
    public WebElement getEmailInput() {
        return emailInput;
    }
}
