package es.subbotin.qalab.tests;

import es.subbotin.qalab.base.BaseTest;
import es.subbotin.qalab.pages.DynamicVisibilitySection;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the Dynamic Visibility section.
 * Checkbox label: "I agree to terms and conditions".
 * Secret panel text: "Secret panel revealed! This element is hidden until checkbox is checked."
 */
public class DynamicVisibilityTest extends BaseTest {

    /**
     * Secret panel must be hidden on initial page load.
     */
    @Test(groups = {"regression"}, description = "Secret panel is hidden before the checkbox is checked")
    public void panelIsHiddenOnPageLoad() {
        DynamicVisibilitySection dynVis = new DynamicVisibilitySection(getDriver());
        Assert.assertFalse(dynVis.isPanelVisible(),
            "Secret panel should be hidden before the checkbox is checked");
    }

    /**
     * Checking the checkbox must reveal the secret panel.
     */
    @Test(groups = {"regression"}, description = "Checking the checkbox reveals the secret panel")
    public void panelBecomesVisibleWhenCheckboxChecked() {
        DynamicVisibilitySection dynVis = new DynamicVisibilitySection(getDriver());
        dynVis.showPanel();
        Assert.assertTrue(dynVis.getSecretPanel().isDisplayed(),
            "Secret panel should be visible after checking 'I agree to terms and conditions'");
    }

    /**
     * Unchecking the checkbox must hide the secret panel again.
     */
    @Test(groups = {"regression"}, description = "Unchecking the checkbox hides the secret panel again")
    public void panelHidesWhenCheckboxUnchecked() {
        DynamicVisibilitySection dynVis = new DynamicVisibilitySection(getDriver());
        dynVis.showPanel();
        dynVis.hidePanel();
        Assert.assertFalse(dynVis.isPanelVisible(),
            "Secret panel should be hidden after unchecking the checkbox");
    }

    /**
     * Toggle checkbox state must match its visual checked/unchecked appearance.
     */
    @Test(groups = {"regression"}, description = "Toggle checkbox state matches the panel visible/hidden state")
    public void toggleCheckboxStateIsConsistent() {
        DynamicVisibilitySection dynVis = new DynamicVisibilitySection(getDriver());
        dynVis.showPanel();
        Assert.assertTrue(dynVis.getToggleCheckbox().isSelected(),
            "Toggle checkbox should be selected when panel is shown");
        dynVis.hidePanel();
        Assert.assertFalse(dynVis.getToggleCheckbox().isSelected(),
            "Toggle checkbox should be deselected when panel is hidden");
    }
}
