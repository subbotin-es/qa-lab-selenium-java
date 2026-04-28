package es.subbotin.qalab.tests;

import es.subbotin.qalab.base.BaseTest;
import es.subbotin.qalab.pages.ModalsSection;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the Modals section.
 * Modal title: "Test Modal Window". Triggered by "Open Modal" button.
 * Modal buttons: Confirm, Cancel.
 */
public class ModalsTest extends BaseTest {

    /**
     * Clicking Open Modal must make the dialog visible.
     */
    @Test(groups = {"smoke", "regression"}, description = "Open Modal button makes the dialog visible")
    public void openModalOpensDialog() {
        ModalsSection modals = new ModalsSection(getDriver());
        modals.open();
        Assert.assertTrue(modals.getModalContainer().isDisplayed(),
            "Modal dialog should be visible after clicking Open Modal");
    }

    /**
     * X close button must dismiss the modal.
     * Note: #modal-confirm has no JS handler on this page — close paths are Cancel and X only.
     */
    @Test(groups = {"regression"}, description = "X close button dismisses the modal dialog")
    public void closeButtonDismissesModal() {
        ModalsSection modals = new ModalsSection(getDriver());
        modals.open();
        modals.getModalContainer();
        modals.close();
        modals.waitForModalClosed();
        Assert.assertFalse(modals.isModalVisible(),
            "Modal should not be visible after clicking the X close button");
    }

    /**
     * Cancel button must close the modal without confirming.
     */
    @Test(groups = {"regression"}, description = "Cancel button closes the modal dialog without confirming")
    public void cancelClosesModal() {
        ModalsSection modals = new ModalsSection(getDriver());
        modals.open();
        modals.getModalContainer();
        modals.cancel();
        modals.waitForModalClosed();
        Assert.assertFalse(modals.isModalVisible(),
            "Modal should not be visible after clicking Cancel");
    }
}
