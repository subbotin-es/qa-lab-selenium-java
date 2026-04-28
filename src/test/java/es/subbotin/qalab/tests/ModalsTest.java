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
    @Test(groups = {"smoke", "regression"})
    public void openModalOpensDialog() {
        ModalsSection modals = new ModalsSection(getDriver());
        modals.open();
        Assert.assertTrue(modals.getModalContainer().isDisplayed(),
            "Modal dialog should be visible after clicking Open Modal");
    }

    /**
     * Confirm button must close the modal.
     */
    @Test(groups = {"regression"})
    public void confirmClosesModal() {
        ModalsSection modals = new ModalsSection(getDriver());
        modals.open();
        modals.getModalContainer();
        modals.confirm();
        modals.waitForModalClosed();
        Assert.assertFalse(modals.isModalVisible(),
            "Modal should not be visible after clicking Confirm");
    }

    /**
     * Cancel button must close the modal without confirming.
     */
    @Test(groups = {"regression"})
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
