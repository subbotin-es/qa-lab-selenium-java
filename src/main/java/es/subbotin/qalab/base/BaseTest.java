package es.subbotin.qalab.base;

import es.subbotin.qalab.config.ConfigReader;
import es.subbotin.qalab.listeners.ExtentReportsListener;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

/**
 * Base test class — manages WebDriver lifecycle for all tests.
 * ThreadLocal ensures parallel test safety.
 */
@Listeners(ExtentReportsListener.class)
public class BaseTest {

    /** ThreadLocal ensures each parallel thread has its own driver instance. */
    private static final ThreadLocal<WebDriver> DRIVER_HOLDER = new ThreadLocal<>();

    /**
     * Returns the WebDriver instance for the current thread.
     *
     * @return WebDriver instance
     */
    protected static WebDriver getDriver() {
        return DRIVER_HOLDER.get();
    }

    /**
     * Initialises WebDriver before each test method.
     * Navigates to QA Lab target page.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        DRIVER_HOLDER.set(new ChromeDriver(options));
        getDriver().get(ConfigReader.getBaseUrl() + "/QA-Lab/qa-lab.html");
    }

    /**
     * Quits WebDriver after each test method.
     * Always runs — even if test fails.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            DRIVER_HOLDER.remove();
        }
    }
}