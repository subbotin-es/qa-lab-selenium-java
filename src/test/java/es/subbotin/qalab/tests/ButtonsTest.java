package es.subbotin.qalab.tests;

import es.subbotin.qalab.base.BaseTest;
import es.subbotin.qalab.pages.ButtonsSection;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the Buttons section.
 * Covers click states and disabled-state assertion.
 */
public class ButtonsTest extends BaseTest {

    /**
     * Disabled Button must carry the HTML disabled attribute.
     * Verifies the Page Object correctly reads the element state.
     */
    @Test(groups = {"smoke", "regression"}, description = "Disabled Button carries the HTML disabled attribute")
    public void disabledButtonIsNotEnabled() {
        ButtonsSection buttons = new ButtonsSection(getDriver());
        Assert.assertTrue(buttons.isDisabled(),
            "Disabled Button should not be enabled");
    }

    /**
     * Primary Button must be visible and interactable.
     */
    @Test(groups = {"regression"}, description = "Primary Button is visible and interactable")
    public void primaryButtonIsEnabled() {
        ButtonsSection buttons = new ButtonsSection(getDriver());
        Assert.assertTrue(buttons.isPrimaryEnabled(),
            "Primary Button should be enabled and visible");
    }

    /**
     * Danger Button must be visible and interactable.
     */
    @Test(groups = {"regression"}, description = "Danger Button is visible and interactable")
    public void dangerButtonIsEnabled() {
        ButtonsSection buttons = new ButtonsSection(getDriver());
        Assert.assertTrue(buttons.isDangerEnabled(),
            "Danger Button should be enabled and visible");
    }

    /**
     * Clicking Primary Button should not navigate the page away.
     */
    @Test(groups = {"regression"}, description = "Clicking Primary Button does not navigate away from qa-lab.html")
    public void clickPrimaryButtonStaysOnPage() {
        ButtonsSection buttons = new ButtonsSection(getDriver());
        buttons.clickPrimary();
        Assert.assertTrue(getDriver().getCurrentUrl().contains("qa-lab.html"),
            "Page URL should remain on qa-lab.html after clicking Primary Button");
    }

    /**
     * Clicking Danger Button should not navigate the page away.
     */
    @Test(groups = {"regression"}, description = "Clicking Danger Button does not navigate away from qa-lab.html")
    public void clickDangerButtonStaysOnPage() {
        ButtonsSection buttons = new ButtonsSection(getDriver());
        buttons.clickDanger();
        Assert.assertTrue(getDriver().getCurrentUrl().contains("qa-lab.html"),
            "Page URL should remain on qa-lab.html after clicking Danger Button");
    }
}
