package es.subbotin.qalab.tests;

import es.subbotin.qalab.base.BaseTest;
import es.subbotin.qalab.pages.InputsSection;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the Input Fields section.
 * Covers text, number, date, search, and URL input types.
 */
public class InputsTest extends BaseTest {

    /**
     * Text input should retain the typed value.
     */
    @Test(groups = {"regression"})
    public void textInputAcceptsValue() {
        InputsSection inputs = new InputsSection(getDriver());
        inputs.fillText("Hello World");
        Assert.assertEquals(inputs.getTextInput().getAttribute("value"), "Hello World",
            "Text input should contain the typed value");
    }

    /**
     * Number input should retain the typed numeric value.
     */
    @Test(groups = {"regression"})
    public void numberInputAcceptsValue() {
        InputsSection inputs = new InputsSection(getDriver());
        inputs.fillNumber("42");
        Assert.assertEquals(inputs.getNumberInput().getAttribute("value"), "42",
            "Number input should contain the typed value");
    }

    /**
     * Date input should retain the typed date string.
     */
    @Test(groups = {"regression"})
    public void dateInputAcceptsValue() {
        InputsSection inputs = new InputsSection(getDriver());
        inputs.fillDate("2024-06-15");
        Assert.assertFalse(inputs.getDateInput().getAttribute("value").isEmpty(),
            "Date input should not be empty after setting a date");
    }

    /**
     * Search input should retain the typed search query.
     */
    @Test(groups = {"regression"})
    public void searchInputAcceptsValue() {
        InputsSection inputs = new InputsSection(getDriver());
        inputs.fillSearch("selenium");
        Assert.assertEquals(inputs.getSearchInput().getAttribute("value"), "selenium",
            "Search input should contain the typed query");
    }

    /**
     * URL input should retain the typed URL.
     */
    @Test(groups = {"regression"})
    public void urlInputAcceptsValue() {
        InputsSection inputs = new InputsSection(getDriver());
        inputs.fillUrl("https://example.com");
        Assert.assertEquals(inputs.getUrlInput().getAttribute("value"), "https://example.com",
            "URL input should contain the typed URL");
    }
}
