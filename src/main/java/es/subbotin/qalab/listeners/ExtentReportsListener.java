package es.subbotin.qalab.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener — populates ExtentReports automatically.
 * ISuiteListener hooks fire once per suite, preventing the report from being
 * overwritten when testng.xml has multiple test elements.
 */
public class ExtentReportsListener implements ITestListener, ISuiteListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> TEST_HOLDER = new ThreadLocal<>();

    /**
     * Initialises ExtentReports once per suite.
     * Path is overridable via -Dextent.report.path for separate smoke/regression reports.
     *
     * @param suite TestNG suite
     */
    @Override
    public void onStart(ISuite suite) {
        String reportPath = System.getProperty("extent.report.path") != null
                ? System.getProperty("extent.report.path")
                : "test-output/ExtentReports/regression-report.html";
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("QA Lab — Selenium Java Tests");
        spark.config().setReportName("Cross-Stack Series — Stack 3");
        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Author", "Evgenii Subbotin");
        extent.setSystemInfo("Target", "https://subbotin.es/QA-Lab/qa-lab.html");
        extent.setSystemInfo("Stack", "Selenium 4 + Java 17 + TestNG 7");
    }

    /**
     * Flushes report once after all tests in the suite complete.
     *
     * @param suite TestNG suite
     */
    @Override
    public void onFinish(ISuite suite) {
        extent.flush();
    }

    /**
     * Creates a new test entry in the report.
     * Uses @Test(description) when provided, falls back to method name.
     *
     * @param result TestNG result
     */
    @Override
    public void onTestStart(ITestResult result) {
        String description = result.getMethod().getDescription();
        String name = (description != null && !description.isEmpty())
                ? description
                : result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(name);
        TEST_HOLDER.set(test);
    }

    /**
     * Marks test as passed.
     *
     * @param result TestNG result
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        TEST_HOLDER.get().pass("Test passed");
    }

    /**
     * Marks test as failed with throwable.
     *
     * @param result TestNG result
     */
    @Override
    public void onTestFailure(ITestResult result) {
        TEST_HOLDER.get().fail(result.getThrowable());
    }

    /**
     * Marks test as skipped.
     *
     * @param result TestNG result
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        TEST_HOLDER.get().skip(result.getThrowable());
    }

    /**
     * Returns the ExtentTest instance for the current thread.
     *
     * @return ExtentTest instance
     */
    public static ExtentTest getTest() {
        return TEST_HOLDER.get();
    }
}
