package es.subbotin.qalab.data;

import org.testng.annotations.DataProvider;

/**
 * TestNG DataProvider methods for form and input test parametrisation.
 */
public class FormDataProvider {

    /**
     * Valid registration form data — three realistic user records.
     * Parallel execution enabled so each row runs in its own thread.
     *
     * @return 2D array of {fullName, email, age, phone}
     */
    @DataProvider(name = "validFormData", parallel = true)
    public static Object[][] validFormData() {
        return new Object[][] {
            {"John Doe",    "john@example.com",  30, "+1234567890"},
            {"Jane Smith",  "jane@example.com",  25, "+9876543210"},
            {"Bob Johnson", "bob@example.com",   40, "+1122334455"},
        };
    }

    /**
     * Invalid email addresses used to assert form validation behaviour.
     *
     * @return 2D array of {email}
     */
    @DataProvider(name = "invalidEmails")
    public static Object[][] invalidEmails() {
        return new Object[][] {
            {"notanemail"},
            {"missing@"},
            {"@nodomain.com"},
        };
    }

    /**
     * Valid text input samples for the Inputs section.
     *
     * @return 2D array of {textValue, numberValue, dateValue, searchQuery, urlValue}
     */
    @DataProvider(name = "inputFieldData")
    public static Object[][] inputFieldData() {
        return new Object[][] {
            {"Hello World", "42",  "2024-06-15", "selenium",  "https://example.com"},
            {"Test input",  "100", "2025-01-01", "testng",    "https://subbotin.es"},
        };
    }
}
