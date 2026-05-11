# QA Lab — Selenium + Java + TestNG

[![CI](https://github.com/subbotin-es/qa-lab-selenium-java/actions/workflows/ci.yml/badge.svg)](https://github.com/subbotin-es/qa-lab-selenium-java/actions/workflows/ci.yml)

**Portfolio artefact #3 — Cross-Stack QA Automation Series**

**Live ExtentReports:**
- 🔵 [Full Regression Report](https://subbotin-es.github.io/qa-lab-selenium-java/extent/regression-report.html)
- 🟡 [Smoke Report](https://subbotin-es.github.io/qa-lab-selenium-java/extent/smoke-report.html)

---

## Purpose

This project demonstrates enterprise-grade Selenium + Java automation against a
live QA practice target — the same target used in all four stacks of the series.

**Why this stack still matters:**
Selenium + Java is the dominant stack in financial services, insurance, energy,
and enterprise IT. These organisations have large existing test codebases, internal
frameworks built on WebDriver, and hiring pipelines tuned for Java fluency.
This project demonstrates the ability to operate in those environments — without
advocating for them over modern alternatives.

Target: [subbotin.es/QA-Lab/qa-lab.html](https://subbotin.es/QA-Lab/qa-lab.html)

---

## Tech Stack

| Layer             | Technology          | Version  | Why                                                    |
|-------------------|---------------------|----------|--------------------------------------------------------|
| Test framework    | TestNG              | 7.10.2   | Enterprise standard — DataProvider, parallel groups    |
| Browser automation| Selenium WebDriver  | 4.21.0   | Industry standard for Java, expected in enterprise JDs |
| Driver management | WebDriverManager    | 5.8.0    | Eliminates manual chromedriver downloads               |
| Reporting         | ExtentReports       | 5.1.1    | Standard free HTML reporter for Java CI pipelines      |
| Build tool        | Maven               | 3.9+     | Universal in Java enterprises, Surefire plugin         |
| Language          | Java                | 17 LTS   | LTS release, widely deployed in enterprise             |
| Lint / style      | Checkstyle          | 10.x     | Enforces code style expected in enterprise teams       |
| CI/CD             | GitHub Actions      | current  | Free 2 000 min/month                                   |
| Performance       | Gatling             | 3.11.5   | Java DSL, Maven plugin — zero toolchain friction       |

---

## Run Locally

```bash
# 1. Clone and enter the project
git clone https://github.com/subbotin-es/qa-lab-selenium-java.git
cd qa-lab-selenium-java

# 2. Run the smoke suite (ButtonsTest, FormsTest, ModalsTest)
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-smoke.xml

# 3. Run the full regression suite
mvn test

# 4. Run Gatling smoke simulation (SLO check)
mvn test-compile gatling:test -Pperformance \
  -Dgatling.simulationClass=simulations.QALabSmokeSimulation

# 5. Run Gatling baseline simulation
mvn test-compile gatling:test -Pperformance \
  -Dgatling.simulationClass=simulations.QALabBaselineSimulation
```

Functional report: `test-output/ExtentReports/report.html`
Gatling report: `performance/target/gatling/<timestamp>/index.html`

**Prerequisites:** Java 17, Maven 3.9+, Chrome installed (WebDriverManager downloads chromedriver automatically).

---

## Test Coverage

| Section             | What is tested                                           |
|---------------------|----------------------------------------------------------|
| Buttons             | Click states, disabled-state assertion                   |
| Forms               | Field interaction, DataProvider parametrisation, submit  |
| Input Fields        | text, number, date, search, URL types                    |
| Checkboxes          | check/uncheck, disabled state, pre-checked defaults      |
| Radio Buttons       | selection, mutual exclusivity                            |
| Dropdowns           | Select class — single select, multi-select               |
| Tables              | Row count, cell content, email format                    |
| Alerts / Modals     | Open, confirm, cancel, close wait                        |
| Dynamic Visibility  | Checkbox-triggered panel reveal and hide                 |
| Async Buttons       | Loading → success/error state transitions                |
| IFrames             | switchTo().frame() / switchTo().defaultContent()         |
| Drag & Drop         | Actions class mouse simulation                           |
| Slider              | Actions.dragAndDropBy() — pixel-to-value mapping         |

---

## Performance Testing

Gatling 3.11 is embedded as a Maven profile (`-Pperformance`), keeping it fully isolated from the TestNG test suite. The performance job runs in CI after all functional tests pass.

**Gatling HTML report** is uploaded as a CI artifact on every run (14-day retention) — download from the [Actions tab](https://github.com/subbotin-es/qa-lab-selenium-java/actions).

### Simulations

| Simulation | Load profile | SLO assertions |
|---|---|---|
| `QALabSmokeSimulation` | Ramp 5 VU over 10 s → hold 5 VU/s for 20 s | p95 < 500 ms, p99 < 1 000 ms, errors < 1% |
| `QALabBaselineSimulation` | Ramp 10 VU over 15 s → hold 10 VU/s for 45 s | p95 < 500 ms, p99 < 1 000 ms, errors < 1%, success > 99% |
| `QALabColdWarmSimulation` | 1 VU, 2 sequential requests | cold\_hit p95 < 1 500 ms, warm\_hit p95 < 200 ms |

### Observed CI results (CloudFront edge, GitHub Actions ubuntu-latest)

| Metric | Smoke | Baseline |
|---|---|---|
| p75 response time | ~15 ms | ~24 ms |
| p95 response time | ~20 ms | ~27 ms |
| p99 response time | ~25 ms | ~32 ms |
| Error rate | < 1% | < 1% |
| All SLOs passed | Yes | Yes |

> Response times reflect CloudFront edge-cache delivery of a static page (~50 KB HTML + JS). They measure CDN serving speed, not application performance. See [performance/FINDINGS.md](performance/FINDINGS.md) for full analysis, assumptions, and limitations.

---

## Java vs Playwright — Comparison

*This project exists to demonstrate enterprise-stack fluency. The author advocates Playwright for new projects.*

| Aspect              | Selenium + Java (Stack 3)                   | Playwright TS (Stack 1)            |
|---------------------|---------------------------------------------|------------------------------------|
| Verbosity           | High — PageFactory, @FindBy, WebDriverWait  | Low — auto-waiting built in        |
| Parallel execution  | ThreadLocal driver, testng.xml config       | `fullyParallel: true`              |
| Data-driven tests   | @DataProvider                               | test.each or parametrize           |
| Reporting           | ExtentReports (manual listener wiring)      | Allure (built-in integration)      |
| Driver management   | WebDriverManager                            | Bundled browsers                   |
| CI run time         | ~3–4 min                                    | ~1–2 min                           |
| Enterprise fit      | High — familiar to any Java shop            | Growing — newer enterprises        |

---

## Known Limitations

| Topic                 | Decision                                    | Rationale                                                         |
|-----------------------|---------------------------------------------|-------------------------------------------------------------------|
| Drag & Drop           | Actions class mouse simulation              | HTML5 drag events not directly supported by WebDriver Actions     |
| IFrame handling       | switchTo().frame() → switchTo().default()   | Classic pattern — must always return to default context           |
| Parallel safety       | ThreadLocal WebDriver                       | Required — Selenium WebDriver is not thread-safe                  |
| Screenshots on failure| Via ITestListener                           | Cannot use @Attachment like Allure — different ecosystem          |
| Browser support       | Chrome only in CI                           | Firefox adds geckodriver complexity for minimal portfolio gain     |
| CDP version warning   | Chrome 147 vs Selenium 4.21.0              | Harmless — bump `selenium.version` in pom.xml to resolve          |

---

## Cross-Stack Series

All four stacks automate the same target — [subbotin.es/QA-Lab/qa-lab.html](https://subbotin.es/QA-Lab/qa-lab.html) — enabling a direct comparison of tooling decisions.

| # | Stack | Repo |
|---|---|---|
| 1 | Playwright + TypeScript | [qa-lab-playwright](https://github.com/subbotin-es/qa-lab-playwright) |
| 2 | Pytest + Python + Allure  | [qa-lab-pytest-python](https://github.com/subbotin-es/qa-lab-pytest-python) |
| 3 | **Selenium + Java + TestNG** <-This repo | [qa-lab-selenium-java](https://github.com/subbotin-es/qa-lab-selenium-java) |
| 4 | Cypress + JavaScript | [qa-lab-cypress](https://github.com/subbotin-es/qa-lab-cypress) |
| 5 | Playwright + C# + NUnit | [qa-lab-playwright-csharp](https://github.com/subbotin-es/qa-lab-playwright-csharp)        |

---

*Author: Evgenii Subbotin — [subbotin.es](https://subbotin.es)*
