package es.subbotin.qalab.tests;

import es.subbotin.qalab.base.BaseTest;
import es.subbotin.qalab.pages.DragDropSection;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the Slider element.
 * Slider range: 0–100, default value: 50.
 * Uses DragDropSection (slider shares the Actions-based drag approach).
 * Actions.dragAndDropBy() is used because direct sendKeys on input[type='range']
 * is unreliable across browsers.
 */
public class SliderTest extends BaseTest {

    /**
     * Slider must report the default value of 50 on page load.
     */
    @Test(groups = {"regression"}, description = "Slider reports default value of 50 on page load")
    public void sliderHasDefaultValueOfFifty() {
        DragDropSection dragDrop = new DragDropSection(getDriver());
        Assert.assertEquals(dragDrop.getSliderValue(), "50",
            "Slider should have a default value of 50");
    }

    /**
     * Moving the slider right must change its value from the default.
     * Exact new value depends on slider width and pixel-to-value ratio.
     */
    @Test(groups = {"regression"}, description = "Slider value changes after dragging 50px to the right")
    public void sliderValueChangesAfterMove() {
        DragDropSection dragDrop = new DragDropSection(getDriver());
        String before = dragDrop.getSliderValue();
        dragDrop.moveSliderBy(50);
        String after = dragDrop.getSliderValue();
        Assert.assertNotEquals(after, before,
            "Slider value should change after dragging 50px to the right");
    }

    /**
     * Slider element must be visible and interactable.
     */
    @Test(groups = {"regression"}, description = "Slider element is visible and interactable")
    public void sliderIsVisible() {
        DragDropSection dragDrop = new DragDropSection(getDriver());
        Assert.assertTrue(dragDrop.getSlider().isDisplayed(),
            "Slider element should be visible on the page");
    }
}
