package es.subbotin.qalab.tests;

import es.subbotin.qalab.base.BaseTest;
import es.subbotin.qalab.pages.AsyncButtonsSection;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the Async Buttons section.
 * Button label: "Claim Your Prize". States cycle: default → loading → success/error.
 */
public class AsyncButtonsTest extends BaseTest {

    /**
     * Async button must be visible and enabled before interaction.
     */
    @Test(groups = {"regression"})
    public void asyncButtonIsEnabledOnPageLoad() {
        AsyncButtonsSection async = new AsyncButtonsSection(getDriver());
        Assert.assertTrue(async.getAsyncButton().isDisplayed(),
            "Claim Your Prize button should be visible on page load");
        Assert.assertTrue(async.getAsyncButton().isEnabled(),
            "Claim Your Prize button should be enabled on page load");
    }

    /**
     * Clicking the async button should produce a non-empty status change.
     * The exact final state text ("Success" / "Error") depends on live page behaviour
     * and may vary across runs — this test waits for any text containing "Success".
     * Adjust expectedState if the live page uses different wording.
     */
    @Test(groups = {"regression"})
    public void asyncButtonProducesSuccessState() {
        AsyncButtonsSection async = new AsyncButtonsSection(getDriver());
        async.click();
        String status = async.waitForState("Success");
        Assert.assertFalse(status.isEmpty(),
            "Status text should not be empty after async operation completes");
    }
}
