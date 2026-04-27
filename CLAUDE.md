# CLAUDE.md — QA Lab: Selenium + Java + TestNG + ExtentReports

> **This file is the authoritative specification for Claude Code.**
> Read it completely before writing any test, any POM class, any config.
> Every architectural decision documented here has a rationale — don't override without explicit instruction.
> When in doubt — ask. Do not invent test cases. Do not skip WebDriverManager setup.

**Author:** Evgenii Subbotin
**Project:** QA Lab Cross-Stack Series — Stack 3: Selenium + Java + TestNG
**Target:** https://subbotin.es/QA-Lab/qa-lab.html
**Stack:** Selenium WebDriver · Java 17 · TestNG · ExtentReports · Maven · GitHub Actions
**Version:** 1.0 | April 2026

---

## 1. What This Project Does

Isolated Selenium + Java + TestNG test framework targeting the QA Lab live environment.
Demonstrates enterprise-grade Java automation practices expected in fintech, energy, and traditional IT organizations: Page Factory pattern, TestNG data providers, ExtentReports HTML output, and Maven-driven CI pipeline.

**This is portfolio artefact #3 in the Cross-Stack Series:**
```
Same target (qa-lab.html) → different stacks → comparative analysis
Stack 1: Playwright + TS
Stack 2: Pytest + Python
Stack 3: Selenium + Java + TestNG    ← this project
Stack 4: Cypress + JS
```

**Why this stack still matters (portfolio talking point):**
Selenium + Java is the dominant stack in financial services, insurance, energy, and enterprise IT.
These organisations have large existing test codebases, internal frameworks built on WebDriver,
and hiring pipelines tuned for Java fluency. This project demonstrates the ability to operate
in those environments without advocating for them over modern alternatives.

**Test coverage scope:**
```
Buttons          → click states, disabled state assertion
Forms            → validation, field interaction, submit
Input Fields     → text, number, date, search, URL types
Checkboxes       → check/uncheck, disabled state
Radio Buttons    → selection, mutual exclusivity
Dropdowns        → Select class, single, multi-select
Tables           → cell content, row count, edit action
Alerts/Modals    → open, confirm, cancel, dismiss
Dynamic Visibility → checkbox-triggered panel reveal
Async Buttons    → loading → success/error state transitions
IFrames          → switchTo().frame() / switchTo().defaultContent()
Drag & Drop      → Actions class
Slider           → Actions.dragAndDropBy()
```

---

## 2. Absolute Rules — Read Before Every Task

```
NEVER use Thread.sleep() — use WebDriverWait with ExpectedConditions
NEVER hardcode base URL — always use BASE_URL from config.properties
NEVER write assertions inside Page Object methods — they return WebElements or values
NEVER use driver.findElement() in test classes — always delegate to Page Objects
NEVER commit driver binaries — WebDriverManager downloads them at runtime
NEVER use @Test(dependsOnMethods=) unless the dependency is documented
NEVER use static WebDriver fields in tests — use ThreadLocal for parallel safety
ALWAYS use WebDriverWait — never implicit waits mixed with explicit waits
ALWAYS use Page Factory (@FindBy) for element declarations
ALWAYS use @DataProvider for parametrized tests — not manual loops
ALWAYS use ExtentReports listeners — no manual report.flush() in test code
ALWAYS use Maven Surefire plugin for test execution — never run tests ad-hoc
ALWAYS ensure driver.quit() is called in @AfterMethod — never in test body
KEEP driver management in BaseTest — never create WebDriver in Page Objects
```

---

## 3. Tech Stack

| Layer | Technology | Version | Why |
|---|---|---|---|
| Test framework | TestNG | 7.10+ | Standard in enterprise Java shops, DataProvider, parallel groups |
| Browser automation | Selenium WebDriver | 4.21+ | Industry standard for Java, expected in enterprise JDs |
| Driver management | WebDriverManager | 5.8+ | Eliminates manual chromedriver downloads |
| Reporting | ExtentReports | 5.1+ | Standard free enterprise HTML reporter for Java |
| Build tool | Maven | 3.9+ | Universal in Java enterprises, Surefire plugin |
| Language | Java | 17 LTS | LTS release, widely deployed in enterprise |
| Lint / style | Checkstyle | 10.x | Enforces code style expected in enterprise teams |
| CI/CD | GitHub Actions | current | Free 2000 min/month |

**No JUnit. No Gradle. No Playwright.**
Budget target: $0/month (all free tiers).

---

## 4. Repository Structure

```
qa-lab-selenium-java/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── es/subbotin/qalab/
│   │           ├── pages/
│   │           │   ├── QALabPage.java              # Base page — driver, wait, navigation
│   │           │   ├── ButtonsSection.java
│   │           │   ├── FormsSection.java
│   │           │   ├── InputsSection.java
│   │           │   ├── CheckboxesSection.java
│   │           │   ├── DropdownsSection.java
│   │           │   ├── TablesSection.java
│   │           │   ├── ModalsSection.java
│   │           │   ├── DynamicVisibilitySection.java
│   │           │   ├── AsyncButtonsSection.java
│   │           │   ├── IFrameSection.java
│   │           │   └── DragDropSection.java
│   │           ├── base/
│   │           │   └── BaseTest.java               # WebDriver init/quit, ExtentReports setup
│   │           ├── config/
│   │           │   └── ConfigReader.java            # Reads config.properties
│   │           ├── listeners/
│   │           │   └── ExtentReportsListener.java   # ITestListener → populates report
│   │           └── data/
│   │               └── FormDataProvider.java        # @DataProvider methods
│   └── test/
│       └── java/
│           └── es/subbotin/qalab/tests/
│               ├── ButtonsTest.java
│               ├── FormsTest.java
│               ├── InputsTest.java
│               ├── CheckboxesTest.java
│               ├── DropdownsTest.java
│               ├── TablesTest.java
│               ├── ModalsTest.java
│               ├── DynamicVisibilityTest.java
│               ├── AsyncButtonsTest.java
│               ├── IFrameTest.java
│               ├── DragDropTest.java
│               └── SliderTest.java
├── src/test/resources/
│   ├── testng.xml                                   # Suite definition
│   ├── testng-smoke.xml                             # Smoke suite only
│   └── config.properties                            # Non-secret config
├── test-output/
│   └── ExtentReports/                               # Generated HTML (git-ignored)
├── .github/
│   └── workflows/
│       └── ci.yml
├── pom.xml
├── checkstyle.xml
└── README.md
```

---

## 5. pom.xml — Exact Dependencies

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>es.subbotin</groupId>
    <artifactId>qa-lab-selenium</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <properties>
        <java.version>17</java.version>
        <maven.compiler.source>${java.version}</maven.compiler.source>
        <maven.compiler.target>${java.version}</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <selenium.version>4.21.0</selenium.version>
        <testng.version>7.10.2</testng.version>
        <webdrivermanager.version>5.8.0</webdrivermanager.version>
        <extentreports.version>5.1.1</extentreports.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>${selenium.version}</version>
        </dependency>
        <dependency>
            <groupId>io.github.bonigarcia</groupId>
            <artifactId>webdrivermanager</artifactId>
            <version>${webdrivermanager.version}</version>
        </dependency>
        <dependency>
            <groupId>org.testng</groupId>
            <artifactId>testng</artifactId>
            <version>${testng.version}</version>
        </dependency>
        <dependency>
            <groupId>com.aventstack</groupId>
            <artifactId>extentreports</artifactId>
            <version>${extentreports.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
                <configuration>
                    <suiteXmlFiles>
                        <suiteXmlFile>src/test/resources/testng.xml</suiteXmlFile>
                    </suiteXmlFiles>
                    <!-- Thread-safe parallel execution -->
                    <parallel>methods</parallel>
                    <threadCount>4</threadCount>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-checkstyle-plugin</artifactId>
                <version>3.4.0</version>
                <configuration>
                    <configLocation>checkstyle.xml</configLocation>
                    <failsOnError>true</failsOnError>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 6. BaseTest.java — Driver Management

```java
// src/main/java/es/subbotin/qalab/base/BaseTest.java
package es.subbotin.qalab.base;

import es.subbotin.qalab.config.ConfigReader;
import es.subbotin.qalab.listeners.ExtentReportsListener;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

// Listener wires ExtentReports automatically — no manual report.flush() in tests
@Listeners(ExtentReportsListener.class)
public class BaseTest {

    // ThreadLocal ensures each parallel thread has its own driver instance
    private static final ThreadLocal<WebDriver> driverHolder = new ThreadLocal<>();

    protected static WebDriver getDriver() {
        return driverHolder.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        driverHolder.set(new ChromeDriver(options));
        getDriver().get(ConfigReader.getBaseUrl() + "/QA-Lab/qa-lab.html");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driverHolder.remove();
        }
    }
}
```

---

## 7. Page Object Pattern — Page Factory Standard

```java
// src/main/java/es/subbotin/qalab/pages/FormsSection.java
package es.subbotin.qalab.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class FormsSection {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Page Factory — @FindBy declarations
    @FindBy(css = "input[placeholder='Full Name']")
    private WebElement fullNameInput;

    @FindBy(css = "input[type='email']")
    private WebElement emailInput;

    @FindBy(css = "input[type='number']")
    private WebElement ageInput;

    @FindBy(css = "input[type='tel']")
    private WebElement phoneInput;

    @FindBy(xpath = "//button[text()='Register']")
    private WebElement registerButton;

    public FormsSection(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void fillForm(String fullName, String email, int age, String phone) {
        wait.until(ExpectedConditions.visibilityOf(fullNameInput));
        fullNameInput.clear();
        fullNameInput.sendKeys(fullName);
        emailInput.clear();
        emailInput.sendKeys(email);
        ageInput.clear();
        ageInput.sendKeys(String.valueOf(age));
        phoneInput.clear();
        phoneInput.sendKeys(phone);
    }

    public void submit() {
        wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        registerButton.click();
    }

    // Returns element for assertion in test class — no assertions here
    public WebElement getRegisterButton() {
        return registerButton;
    }
}
```

---

## 8. ExtentReports Listener

```java
// src/main/java/es/subbotin/qalab/listeners/ExtentReportsListener.java
package es.subbotin.qalab.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportsListener implements ITestListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testHolder = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReports/report.html");
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("QA Lab — Selenium Java Tests");
        spark.config().setReportName("Cross-Stack Series — Stack 3");
        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Author", "Evgenii Subbotin");
        extent.setSystemInfo("Target", "https://subbotin.es/QA-Lab/qa-lab.html");
        extent.setSystemInfo("Stack", "Selenium 4 + Java 17 + TestNG 7");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        testHolder.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testHolder.get().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testHolder.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testHolder.get().skip(result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    public static ExtentTest getTest() {
        return testHolder.get();
    }
}
```

---

## 9. IFrame Handling

```java
// src/main/java/es/subbotin/qalab/pages/IFrameSection.java
package es.subbotin.qalab.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class IFrameSection {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Selector for the iframe — adjust to match actual QA Lab HTML
    private static final By IFRAME_LOCATOR = By.cssSelector("#iframes iframe");

    public IFrameSection(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void switchToFrame() {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(IFRAME_LOCATOR));
    }

    public String getInnerHeadingText() {
        switchToFrame();
        WebElement heading = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1, h2, h3"))
        );
        String text = heading.getText();
        driver.switchTo().defaultContent();  // ALWAYS return to main context
        return text;
    }
}
```

---

## 10. TestNG DataProvider Pattern

```java
// src/main/java/es/subbotin/qalab/data/FormDataProvider.java
package es.subbotin.qalab.data;

import org.testng.annotations.DataProvider;

public class FormDataProvider {

    @DataProvider(name = "validFormData", parallel = true)
    public static Object[][] validFormData() {
        return new Object[][] {
            { "John Doe",     "john@example.com",  30, "+1234567890" },
            { "Jane Smith",   "jane@example.com",  25, "+9876543210" },
            { "Bob Johnson",  "bob@example.com",   40, "+1122334455" },
        };
    }

    @DataProvider(name = "invalidEmails")
    public static Object[][] invalidEmails() {
        return new Object[][] {
            { "notanemail" },
            { "missing@" },
            { "@nodomain.com" },
        };
    }
}
```

---

## 11. testng.xml — Suite Definition

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="QA Lab — Full Regression" parallel="methods" thread-count="4" verbose="1">

    <listeners>
        <listener class-name="es.subbotin.qalab.listeners.ExtentReportsListener"/>
    </listeners>

    <test name="Buttons">
        <classes>
            <class name="es.subbotin.qalab.tests.ButtonsTest"/>
        </classes>
    </test>

    <test name="Forms">
        <classes>
            <class name="es.subbotin.qalab.tests.FormsTest"/>
        </classes>
    </test>

    <test name="IFrames">
        <classes>
            <class name="es.subbotin.qalab.tests.IFrameTest"/>
        </classes>
    </test>

    <!-- Add all other test classes -->

</suite>
```

```xml
<!-- testng-smoke.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="QA Lab — Smoke" verbose="1">
    <test name="Smoke">
        <groups>
            <run>
                <include name="smoke"/>
            </run>
        </groups>
        <classes>
            <class name="es.subbotin.qalab.tests.ButtonsTest"/>
            <class name="es.subbotin.qalab.tests.FormsTest"/>
            <class name="es.subbotin.qalab.tests.ModalsTest"/>
        </classes>
    </test>
</suite>
```

---

## 12. CI/CD Pipeline

```yaml
# .github/workflows/ci.yml
name: QA Lab — Selenium Java + ExtentReports

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]
  schedule:
    - cron: '0 10 * * 1'   # Monday 10:00 UTC

jobs:
  test:
    name: Run TestNG Tests
    runs-on: ubuntu-latest
    timeout-minutes: 30

    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: 'maven'

      - name: Run smoke tests
        run: mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-smoke.xml
        env:
          BASE_URL: https://subbotin.es

      - name: Run full regression
        if: github.ref == 'refs/heads/main'
        run: mvn test
        env:
          BASE_URL: https://subbotin.es

      - name: Upload ExtentReports artifact
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: extent-report
          path: test-output/ExtentReports/
          retention-days: 30

      - name: Deploy ExtentReport to GitHub Pages
        if: github.ref == 'refs/heads/main' && always()
        uses: peaceiris/actions-gh-pages@v4
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}
          publish_dir: ./test-output/ExtentReports
          destination_dir: extent
```

---

## 13. Infrastructure Setup — Step by Step

### Step 1: Java Prerequisites

```bash
# Java 17 LTS — via SDKMAN (recommended)
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 17.0.11-tem

# Maven
sdk install maven 3.9.7

# Verify
java -version    # 17.x
mvn -version     # 3.9.x
```

### Step 2: GitHub Repository Setup

```bash
# Create repo: qa-lab-selenium-java at https://github.com/new
git clone https://github.com/YOUR_USERNAME/qa-lab-selenium-java.git
cd qa-lab-selenium-java

cat > .gitignore << 'EOF'
target/
test-output/
.idea/
*.iml
.DS_Store
*.class
EOF

git add .gitignore
git commit -m "chore: initial gitignore"
git push
```

### Step 3: Register Accounts

**GitHub** (existing):
- Enable GitHub Pages: Settings → Pages → Branch: `gh-pages`

No other accounts needed.

### Step 4: Create Maven Project Structure

```bash
# Create directory structure manually or via Maven archetype
mvn archetype:generate \
  -DgroupId=es.subbotin \
  -DartifactId=qa-lab-selenium \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DarchetypeVersion=1.4 \
  -DinteractiveMode=false
```

### Step 5: config.properties

```properties
# src/test/resources/config.properties
base.url=https://subbotin.es
browser=chrome
implicit.wait=0
explicit.wait=10
headless=true
```

```java
// src/main/java/es/subbotin/qalab/config/ConfigReader.java
package es.subbotin.qalab.config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load config.properties", e);
        }
    }

    public static String getBaseUrl() {
        return System.getenv("BASE_URL") != null
            ? System.getenv("BASE_URL")
            : props.getProperty("base.url");
    }
}
```

### Step 6: Verify Local Run

```bash
# Run smoke tests
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-smoke.xml

# Run full suite
mvn test

# Report is at:
open test-output/ExtentReports/report.html
```

---

## 14. Java vs Modern Stacks — Portfolio Talking Point

Document in README to frame this project's purpose:

| Aspect | Selenium + Java (Stack 3) | Playwright TS (Stack 1) |
|---|---|---|
| Verbosity | High — PageFactory, @FindBy, WebDriverWait | Low — auto-waiting built in |
| Parallel execution | ThreadLocal driver, testng.xml | `fullyParallel: true` |
| Data-driven | @DataProvider | test.each or parametrize |
| Reporting | ExtentReports (manual listener) | Allure (built-in integration) |
| Driver management | WebDriverManager | Bundled browsers |
| CI run time | ~3–4 min | ~1–2 min |
| Enterprise fit | High — familiar to any Java shop | Growing — newer enterprises |

*This project exists to demonstrate enterprise-stack fluency. The author advocates Playwright for new projects.*

---

## 15. Known Limitations & Trade-offs

| Topic | Decision | Rationale |
|---|---|---|
| Drag & Drop | Actions class mouse simulation | HTML5 drag events not directly supported by WebDriver Actions |
| IFrame handling | switchTo().frame() then defaultContent() | Classic Selenium pattern — must always return to default context |
| Parallel safety | ThreadLocal WebDriver | Required — Selenium WebDriver is not thread-safe |
| Screenshots on failure | Via ITestListener | Cannot use @Attachment like Allure — different ecosystem |
| Browser support | Chrome only in CI | Firefox requires geckodriver setup; adds CI complexity for minimal gain |

---

## 16. Definition of Done — Per Task

```
□ mvn compile — zero errors
□ mvn checkstyle:check — zero violations
□ Test has @Test(groups = {"smoke"}) or @Test(groups = {"regression"})
□ Test has at minimum one Assert.* call
□ Page Object uses Page Factory (@FindBy) — no driver.findElement() in test
□ No Thread.sleep() anywhere in diff
□ Smoke tests pass locally: mvn test -Dsurefire.suiteXmlFiles=testng-smoke.xml
□ Commit message: test(section-name): describe what is tested
```

---

## 17. Day-by-Day Prompts for Claude Code

### DAY 1 PROMPT — Infrastructure

```
Read CLAUDE.md Sections 13, 5 completely before starting.

Goal: Maven project compiles, zero test code.

Tasks in order:
1. Verify java -version (17 required) and mvn -version (3.9 required)
2. Generate Maven project structure (Section 13 Step 4)
3. Replace pom.xml with exact content from Section 5
4. Create directory structure: src/main/java/.../pages/, base/, config/, listeners/, data/
5. Create src/test/resources/config.properties (Section 13 Step 5)
6. Create ConfigReader.java (Section 13 Step 5)
7. Create BaseTest.java (Section 6) — ThreadLocal, @BeforeMethod, @AfterMethod
8. Create ExtentReportsListener.java (Section 8)
9. Create testng.xml and testng-smoke.xml (Section 11)
10. Run: mvn compile → must succeed with zero errors
11. Create .gitignore, initial commit, push to GitHub

Do NOT write any test classes today.
```

---

### DAY 2 PROMPT — Page Objects

```
Read CLAUDE.md Sections 7, 9, 10 before starting.

Goal: All Page Objects written with Page Factory pattern.

Implement in order:
1. pages/QALabPage.java — navigate(), scrollToSection(), getTitle()
2. pages/ButtonsSection.java — clickPrimary(), clickDanger(), isDisabled()
3. pages/FormsSection.java — exact pattern from Section 7
4. pages/InputsSection.java — fillText(), fillNumber(), fillDate()
5. pages/CheckboxesSection.java — check(), uncheck(), isChecked()
6. pages/DropdownsSection.java — selectByValue() using Select class
7. pages/TablesSection.java — getRowCount(), getCellText(row, col)
8. pages/ModalsSection.java — open(), confirm(), cancel()
9. pages/DynamicVisibilitySection.java — toggleCheckbox(), isPanelVisible()
10. pages/AsyncButtonsSection.java — click(), waitForState(expectedText)
11. pages/IFrameSection.java — exact pattern from Section 9
12. pages/DragDropSection.java — dragItem(from, to)
13. data/FormDataProvider.java — @DataProvider methods (Section 10)

After each file: mvn compile — fix all errors before continuing.
```

---

### DAY 3 PROMPT — Test Classes

```
Read CLAUDE.md Sections 15, 16 before starting.

Goal: All test classes written, smoke tests pass.

Write in order:
1. tests/ButtonsTest.java — @Test(groups={"smoke"}), @Test(groups={"regression"})
2. tests/FormsTest.java — use @DataProvider from FormDataProvider
3. tests/InputsTest.java
4. tests/CheckboxesTest.java
5. tests/DropdownsTest.java
6. tests/TablesTest.java
7. tests/ModalsTest.java
8. tests/DynamicVisibilityTest.java
9. tests/AsyncButtonsTest.java
10. tests/IFrameTest.java
11. tests/DragDropTest.java
12. tests/SliderTest.java
13. Update testng.xml to include all test classes (Section 11)

Run after each: mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-smoke.xml
```

---

### DAY 4 PROMPT — CI + README

```
Goal: CI green, ExtentReport published to GitHub Pages, README portfolio-ready.

Tasks:
1. Create .github/workflows/ci.yml (Section 12)
2. Push, verify Actions run
3. Check ExtentReport at GitHub Pages URL
4. Write README.md:
   - Purpose: enterprise-stack demonstration (Section 14 framing)
   - Live ExtentReport link
   - Stack table
   - Run locally (3 commands)
   - Java vs Playwright comparison table (Section 14)
   - Known limitations (Section 15)
   - Cross-Stack Series links

Final checklist:
□ GitHub Actions badge green
□ ExtentReport live at GitHub Pages URL
□ All tests passing in CI
□ mvn checkstyle:check passes in CI
```

---

## 18. Common Errors and How to Fix Them

**`java.lang.NoSuchMethodError: WebDriverManager`**
→ Version mismatch between selenium-java and webdrivermanager. Both versions must be compatible. Check pom.xml versions match Section 5.

**`TimeoutException: Expected condition failed`**
→ Element not found in time. Inspect selector with browser DevTools against live QA Lab. Increase `explicit.wait` in config.properties if target is slow.

**`StaleElementReferenceException`**
→ Page re-rendered after element reference was obtained. Re-fetch element via Page Factory or call PageFactory.initElements() again.

**`Cannot find symbol: ExtentTest`**
→ ExtentReports dependency not in pom.xml. Verify Section 5 dependency block is present.

**`Thread-safety error in parallel run`**
→ WebDriver stored as static field (not ThreadLocal). Fix BaseTest to use ThreadLocal<WebDriver> as shown in Section 6.

**`CI fails: Cannot find chromedriver`**
→ WebDriverManager should handle this. Ensure `WebDriverManager.chromedriver().setup()` is called in `@BeforeMethod`. On Ubuntu CI, also add `--no-sandbox` to ChromeOptions.

**`ExtentReport is empty`**
→ Listener not registered. Check testng.xml has the `<listeners>` block from Section 11.

**`mvn checkstyle:check fails`**
→ Code style violation. Run `mvn checkstyle:checkstyle` to see HTML report. Common issues: missing Javadoc, wrong import order, line length.

---

## 19. Branching Strategy

```
main      → production (triggers CI + report deploy)
feat/     → new test sections
fix/      → broken test fixes
chore/    → config, dependency updates
```

Commit message format: `type(scope): description`
Examples:
- `test(forms): add DataProvider for email validation`
- `fix(iframe): update switchTo selector after QA Lab DOM change`
- `chore(deps): bump selenium to 4.22`

---

*End of CLAUDE.md*
*Version: 1.0 | Author: Evgenii Subbotin | Project: QA Lab Cross-Stack Series — Stack 3*
*April 2026*
