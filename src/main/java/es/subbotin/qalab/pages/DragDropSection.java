package es.subbotin.qalab.pages;

import es.subbotin.qalab.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page object for the Drag and Drop section (#drag-drop) and Slider.
 * Drag and drop uses Actions class mouse simulation.
 * Slider uses Actions.dragAndDropBy() since input[type='range'] events
 * are not fully supported by direct sendKeys in all browsers.
 */
public class DragDropSection {

    /** WebDriver instance — needed for Actions builder. */
    private final WebDriver driver;

    /** Explicit wait configured from config.properties. */
    private final WebDriverWait wait;

    /** Draggable item element. */
    @FindBy(css = "#drag-drop .draggable, #drag-drop [draggable='true']")
    private WebElement draggable;

    /** Drop zone target element. */
    @FindBy(css = "#drag-drop .droppable, #drag-drop .drop-zone, #drag-drop .dropzone")
    private WebElement dropZone;

    /** Range slider input. */
    @FindBy(css = "input[type='range']")
    private WebElement slider;

    /**
     * Initialises section with driver and Page Factory.
     *
     * @param driver WebDriver instance from BaseTest
     */
    public DragDropSection(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Drags the default draggable element onto the drop zone.
     */
    public void dragItem() {
        wait.until(ExpectedConditions.visibilityOf(draggable));
        wait.until(ExpectedConditions.visibilityOf(dropZone));
        new Actions(driver)
            .dragAndDrop(draggable, dropZone)
            .perform();
    }

    /**
     * Drags the source element onto the target element.
     *
     * @param from source WebElement
     * @param to   target WebElement
     */
    public void dragItem(WebElement from, WebElement to) {
        wait.until(ExpectedConditions.visibilityOf(from));
        wait.until(ExpectedConditions.visibilityOf(to));
        new Actions(driver)
            .dragAndDrop(from, to)
            .perform();
    }

    /**
     * Moves the slider by a pixel offset from its current position.
     * Uses Actions.dragAndDropBy() — direct slider value manipulation
     * via sendKeys is unreliable across browsers.
     *
     * @param xOffset pixel offset to move (positive = right, negative = left)
     */
    public void moveSliderBy(int xOffset) {
        wait.until(ExpectedConditions.visibilityOf(slider));
        new Actions(driver)
            .dragAndDropBy(slider, xOffset, 0)
            .perform();
    }

    /**
     * Returns the draggable element for assertion in test.
     *
     * @return draggable WebElement
     */
    public WebElement getDraggable() {
        wait.until(ExpectedConditions.visibilityOf(draggable));
        return draggable;
    }

    /**
     * Returns the drop zone element for assertion in test.
     *
     * @return drop zone WebElement
     */
    public WebElement getDropZone() {
        wait.until(ExpectedConditions.visibilityOf(dropZone));
        return dropZone;
    }

    /**
     * Returns the current slider value attribute for assertion in test.
     *
     * @return slider value as String
     */
    public String getSliderValue() {
        wait.until(ExpectedConditions.visibilityOf(slider));
        return slider.getAttribute("value");
    }

    /**
     * Returns the slider element for assertion in test.
     *
     * @return slider WebElement
     */
    public WebElement getSlider() {
        wait.until(ExpectedConditions.visibilityOf(slider));
        return slider;
    }
}
