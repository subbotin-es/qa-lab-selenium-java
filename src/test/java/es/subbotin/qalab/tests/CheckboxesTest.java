package es.subbotin.qalab.tests;

import es.subbotin.qalab.base.BaseTest;
import es.subbotin.qalab.pages.CheckboxesSection;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the Checkboxes and Radio Buttons section.
 * Covers check/uncheck, disabled state, and radio mutual exclusivity.
 * Page layout: Options 1–4 (index 0–3); Option 3 (index 2) pre-checked; Option 4 (index 3) disabled.
 * Radio buttons: Yes (0), No (1), Maybe (2).
 */
public class CheckboxesTest extends BaseTest {

    /**
     * An unchecked checkbox should become checked after calling check().
     */
    @Test(groups = {"regression"}, description = "Unchecked checkbox becomes checked after check()")
    public void checkboxCanBeChecked() {
        CheckboxesSection checkboxes = new CheckboxesSection(getDriver());
        checkboxes.uncheck(0);
        checkboxes.check(0);
        Assert.assertTrue(checkboxes.isChecked(0),
            "Option 1 should be checked after calling check()");
    }

    /**
     * A checked checkbox should become unchecked after calling uncheck().
     */
    @Test(groups = {"regression"}, description = "Checked checkbox becomes unchecked after uncheck()")
    public void checkboxCanBeUnchecked() {
        CheckboxesSection checkboxes = new CheckboxesSection(getDriver());
        checkboxes.check(1);
        checkboxes.uncheck(1);
        Assert.assertFalse(checkboxes.isChecked(1),
            "Option 2 should be unchecked after calling uncheck()");
    }

    /**
     * Option 3 is pre-checked by the page — must be selected on initial load.
     */
    @Test(groups = {"regression"}, description = "Option 3 is pre-checked by the page on initial load")
    public void option3IsPreCheckedByDefault() {
        CheckboxesSection checkboxes = new CheckboxesSection(getDriver());
        Assert.assertTrue(checkboxes.isChecked(2),
            "Option 3 (index 2) should be pre-checked on page load");
    }

    /**
     * Option 4 carries the HTML disabled attribute — must not be interactable.
     */
    @Test(groups = {"regression"}, description = "Option 4 carries the HTML disabled attribute")
    public void option4IsDisabled() {
        CheckboxesSection checkboxes = new CheckboxesSection(getDriver());
        Assert.assertTrue(checkboxes.isCheckboxDisabled(3),
            "Option 4 (index 3) should be disabled");
    }

    /**
     * Clicking a radio button should select it.
     */
    @Test(groups = {"regression"}, description = "Clicking a radio button selects it")
    public void radioButtonCanBeSelected() {
        CheckboxesSection checkboxes = new CheckboxesSection(getDriver());
        checkboxes.selectRadio(0);
        Assert.assertTrue(checkboxes.isRadioSelected(0),
            "Radio button 'Yes' (index 0) should be selected after click");
    }

    /**
     * Selecting a second radio should deselect the first — mutual exclusivity.
     */
    @Test(groups = {"regression"}, description = "Selecting a second radio deselects the first — mutual exclusivity")
    public void radioButtonsMutuallyExclusive() {
        CheckboxesSection checkboxes = new CheckboxesSection(getDriver());
        checkboxes.selectRadio(0);
        checkboxes.selectRadio(1);
        Assert.assertFalse(checkboxes.isRadioSelected(0),
            "Radio 'Yes' should be deselected when 'No' is selected");
        Assert.assertTrue(checkboxes.isRadioSelected(1),
            "Radio 'No' should be selected");
    }
}
