package es.subbotin.qalab.tests;

import es.subbotin.qalab.base.BaseTest;
import es.subbotin.qalab.pages.DropdownsSection;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the Dropdowns section.
 * Single-select options: United States, Canada, United Kingdom, Australia, Other.
 * Multi-select options: Programming, Testing, Design, Marketing.
 */
public class DropdownsTest extends BaseTest {

    /**
     * Single-select should reflect the chosen country.
     */
    @Test(groups = {"regression"})
    public void singleSelectReflectsChoice() {
        DropdownsSection dropdowns = new DropdownsSection(getDriver());
        dropdowns.selectByText("Canada");
        Assert.assertEquals(dropdowns.getSelectedText(), "Canada",
            "Single-select should display 'Canada' after selection");
    }

    /**
     * Choosing a different country should replace the previous selection.
     */
    @Test(groups = {"regression"})
    public void singleSelectCanBeChanged() {
        DropdownsSection dropdowns = new DropdownsSection(getDriver());
        dropdowns.selectByText("United Kingdom");
        dropdowns.selectByText("Australia");
        Assert.assertEquals(dropdowns.getSelectedText(), "Australia",
            "Single-select should reflect the most recent selection");
    }

    /**
     * Multi-select should hold exactly the two chosen options.
     */
    @Test(groups = {"regression"})
    public void multiSelectCanSelectTwoOptions() {
        DropdownsSection dropdowns = new DropdownsSection(getDriver());
        dropdowns.multiSelectByTexts("Testing", "Design");
        Assert.assertEquals(dropdowns.getMultiSelectedOptions().size(), 2,
            "Exactly two options should be selected in the multi-select");
    }

    /**
     * deselectAll inside multiSelectByTexts should clear prior selections first.
     */
    @Test(groups = {"regression"})
    public void multiSelectReplacesExistingSelection() {
        DropdownsSection dropdowns = new DropdownsSection(getDriver());
        dropdowns.multiSelectByTexts("Programming", "Marketing");
        dropdowns.multiSelectByTexts("Testing");
        Assert.assertEquals(dropdowns.getMultiSelectedOptions().size(), 1,
            "Only 'Testing' should be selected after replacing the selection");
    }
}
