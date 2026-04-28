package es.subbotin.qalab.tests;

import es.subbotin.qalab.base.BaseTest;
import es.subbotin.qalab.data.FormDataProvider;
import es.subbotin.qalab.pages.FormsSection;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the Registration Form section.
 * Covers field interaction and form submission.
 */
public class FormsTest extends BaseTest {

    /**
     * Each valid data row should be accepted by all form fields.
     * Verifies the Page Object correctly types into and reads from inputs.
     */
    @Test(groups = {"smoke", "regression"},
          dataProvider = "validFormData",
          dataProviderClass = FormDataProvider.class)
    public void formFieldsAcceptValidInput(String fullName, String email, int age, String phone) {
        FormsSection forms = new FormsSection(getDriver());
        forms.fillForm(fullName, email, age, phone);
        Assert.assertEquals(forms.getFullNameInput().getAttribute("value"), fullName,
            "Full Name field should contain the entered value");
        Assert.assertEquals(forms.getEmailInput().getAttribute("value"), email,
            "Email field should contain the entered value");
    }

    /**
     * Submitting a valid form should not navigate the page away from qa-lab.html.
     */
    @Test(groups = {"regression"},
          dataProvider = "validFormData",
          dataProviderClass = FormDataProvider.class)
    public void submitFormDoesNotNavigateAway(String fullName, String email, int age, String phone) {
        FormsSection forms = new FormsSection(getDriver());
        forms.fillForm(fullName, email, age, phone);
        forms.submit();
        Assert.assertTrue(getDriver().getCurrentUrl().contains("qa-lab.html"),
            "Page URL should remain on qa-lab.html after form submission");
    }
}
