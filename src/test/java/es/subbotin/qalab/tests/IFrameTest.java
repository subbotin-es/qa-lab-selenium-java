package es.subbotin.qalab.tests;

import es.subbotin.qalab.base.BaseTest;
import es.subbotin.qalab.pages.IFrameSection;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the IFrames section.
 * Covers switchTo().frame() and switchTo().defaultContent() round-trip.
 */
public class IFrameTest extends BaseTest {

    /**
     * A heading must be readable from inside the iframe.
     */
    @Test(groups = {"regression"}, description = "A heading element is readable from inside the iframe")
    public void iFrameContainsHeading() {
        IFrameSection iframe = new IFrameSection(getDriver());
        String heading = iframe.getInnerHeadingText();
        Assert.assertFalse(heading.isEmpty(),
            "IFrame should contain a visible heading element");
    }

    /**
     * After getInnerHeadingText() the driver must be back in the main document.
     * If context were still inside the frame, getCurrentUrl() would throw or return
     * the frame src URL — this verifies the defaultContent() switch occurred.
     */
    @Test(groups = {"regression"}, description = "Driver returns to main document context after iframe interaction")
    public void returnsToMainContextAfterFrameInteraction() {
        IFrameSection iframe = new IFrameSection(getDriver());
        iframe.getInnerHeadingText();
        Assert.assertTrue(getDriver().getCurrentUrl().contains("qa-lab.html"),
            "Driver should be back in main context (qa-lab.html) after frame interaction");
    }

    /**
     * Explicit switchToDefault() must leave the driver in the main document context.
     */
    @Test(groups = {"regression"}, description = "switchToDefault() restores the main document context")
    public void switchToDefaultRestoresMainContext() {
        IFrameSection iframe = new IFrameSection(getDriver());
        iframe.switchToFrame();
        iframe.switchToDefault();
        Assert.assertTrue(getDriver().getTitle().length() > 0,
            "Page title should be readable after returning to default content");
    }
}
