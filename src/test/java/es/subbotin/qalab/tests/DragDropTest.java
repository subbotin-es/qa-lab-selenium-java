package es.subbotin.qalab.tests;

import es.subbotin.qalab.base.BaseTest;
import es.subbotin.qalab.pages.DragDropSection;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the Drag and Drop section.
 * Uses Actions class mouse simulation — HTML5 drag events not directly
 * supported by WebDriver, so mouse-based dragAndDrop is used instead.
 */
public class DragDropTest extends BaseTest {

    /**
     * Draggable element must be visible before attempting the drag.
     */
    @Test(groups = {"regression"})
    public void draggableElementIsVisible() {
        DragDropSection dragDrop = new DragDropSection(getDriver());
        Assert.assertTrue(dragDrop.getDraggable().isDisplayed(),
            "Draggable element should be visible in the drag-drop section");
    }

    /**
     * Drop zone must be visible as a valid target.
     */
    @Test(groups = {"regression"})
    public void dropZoneIsVisible() {
        DragDropSection dragDrop = new DragDropSection(getDriver());
        Assert.assertTrue(dragDrop.getDropZone().isDisplayed(),
            "Drop zone should be visible in the drag-drop section");
    }

    /**
     * Performing a drag-and-drop should not throw and should leave the
     * drop zone still visible in the DOM.
     */
    @Test(groups = {"regression"})
    public void canDragItemToDropZone() {
        DragDropSection dragDrop = new DragDropSection(getDriver());
        dragDrop.dragItem();
        Assert.assertTrue(dragDrop.getDropZone().isDisplayed(),
            "Drop zone should remain visible after drag-and-drop operation");
    }
}
