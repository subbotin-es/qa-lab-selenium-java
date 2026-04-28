package es.subbotin.qalab.tests;

import es.subbotin.qalab.base.BaseTest;
import es.subbotin.qalab.pages.TablesSection;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the Tables section.
 * Table columns: ID (1), Name (2), Email (3), Status (4), Action (5).
 * The table contains 3 sample data rows.
 */
public class TablesTest extends BaseTest {

    /**
     * Table must contain exactly 3 sample data rows.
     */
    @Test(groups = {"regression"})
    public void tableHasThreeRows() {
        TablesSection tables = new TablesSection(getDriver());
        Assert.assertEquals(tables.getRowCount(), 3,
            "Table should contain exactly 3 data rows");
    }

    /**
     * Name column in the first row must not be empty.
     */
    @Test(groups = {"regression"})
    public void firstRowNameCellHasContent() {
        TablesSection tables = new TablesSection(getDriver());
        String nameCell = tables.getCellText(1, 2);
        Assert.assertFalse(nameCell.isEmpty(),
            "Row 1, column 2 (Name) should contain text");
    }

    /**
     * Email column in the first row must contain an at-sign.
     */
    @Test(groups = {"regression"})
    public void firstRowEmailCellContainsAtSign() {
        TablesSection tables = new TablesSection(getDriver());
        String emailCell = tables.getCellText(1, 3);
        Assert.assertTrue(emailCell.contains("@"),
            "Row 1, column 3 (Email) should contain a valid email address");
    }

    /**
     * Each of the 3 rows must have a non-empty ID cell.
     */
    @Test(groups = {"regression"})
    public void allRowsHaveNonEmptyId() {
        TablesSection tables = new TablesSection(getDriver());
        int rowCount = tables.getRowCount();
        for (int row = 1; row <= rowCount; row++) {
            Assert.assertFalse(tables.getCellText(row, 1).isEmpty(),
                "Row " + row + " ID cell should not be empty");
        }
    }
}
