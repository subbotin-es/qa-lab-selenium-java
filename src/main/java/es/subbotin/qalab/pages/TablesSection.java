package es.subbotin.qalab.pages;

import es.subbotin.qalab.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page object for the Tables section (#tables).
 * Covers row count, cell text, and Edit action button.
 */
public class TablesSection {

    /** Explicit wait configured from config.properties. */
    private final WebDriverWait wait;

    /** All body rows in the table. */
    @FindBy(css = "#tables tbody tr")
    private List<WebElement> tableRows;

    /**
     * Initialises section with driver and Page Factory.
     *
     * @param driver WebDriver instance from BaseTest
     */
    public TablesSection(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Returns the number of data rows in the table body.
     *
     * @return row count
     */
    public int getRowCount() {
        wait.until(ExpectedConditions.visibilityOfAllElements(tableRows));
        return tableRows.size();
    }

    /**
     * Returns the text of a specific table cell.
     * Row and column are 1-based (CSS nth-child convention).
     *
     * @param row 1-based row index
     * @param col 1-based column index
     * @return cell text
     */
    public String getCellText(int row, int col) {
        String selector = String.format("#tables tbody tr:nth-child(%d) td:nth-child(%d)", row, col);
        WebElement cell = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector))
        );
        return cell.getText();
    }

    /**
     * Clicks the Edit button in the given table row.
     * Row is 1-based.
     *
     * @param row 1-based row index
     */
    public void clickEditInRow(int row) {
        String selector = String.format(
            "#tables tbody tr:nth-child(%d) button, #tables tbody tr:nth-child(%d) a", row, row
        );
        WebElement editButton = wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector(selector))
        );
        editButton.click();
    }

    /**
     * Returns all table rows for assertion in test.
     *
     * @return list of tr WebElements
     */
    public List<WebElement> getTableRows() {
        wait.until(ExpectedConditions.visibilityOfAllElements(tableRows));
        return tableRows;
    }
}
