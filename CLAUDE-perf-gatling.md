# CLAUDE.md — Performance / Gatling (Embedded)
# File location: qa-lab-selenium-java/performance/CLAUDE.md

> **This file is the authoritative specification for Claude Code.**
> Read it completely before writing any simulation, any config, any CI step.
> This is an augmentation of the existing Selenium + Java + TestNG framework — not a standalone project.
> When in doubt — ask. Do not invent scenarios. Do not add Maven dependencies outside this spec.

**Author:** Evgenii Subbotin
**Parent project:** qa-lab-selenium-java (Stack 3 — Cross-Stack Series)
**Performance tool:** Gatling 3.x (Java DSL)
**Target:** https://subbotin.es/QA-Lab/qa-lab.html (S3 + CloudFront)
**Language:** Java — natively consistent with the Maven/TestNG ecosystem of this repo
**Version:** 1.0 | May 2026

---

## 1. What This Augmentation Does

Adds SLO compliance performance testing to the existing Selenium + Java + TestNG framework.
Gatling 3.x with Java DSL is the natural performance companion to this stack:
Maven plugin, Java simulations, enterprise-grade HTML reports, zero toolchain friction.

**Narrative for portfolio / interviews:**
> "Gatling lives in the Java repo because the team that owns Maven owns Gatling.
> Same build tool, same language, same CI. In financial services and enterprise IT —
> exactly where this stack is positioned — Gatling is the recognised performance
> tool that fits without a migration conversation."

**Honest scope (CDN target):**
```
✅ p95 / p99 assertions via Gatling assertions API
✅ Ramp-up scenarios with stages
✅ Gatling HTML report (best-in-class, built-in)
✅ Maven plugin execution — mvn gatling:test
❌ Capacity testing — CDN target does not degrade
❌ Degradation curves — not applicable
```

---

## 2. Absolute Rules

```
NEVER use Scala DSL — Java DSL only, consistent with this repo's language
NEVER add Gatling dependencies to the main test scope — separate Maven module or profile
NEVER exceed 30 users in CI — respect free runner resources
NEVER mix Gatling simulations with TestNG test classes
ALWAYS use Gatling assertions — never manual pass/fail logic
ALWAYS use Maven Gatling plugin — never run Gatling JAR directly
ALWAYS keep simulations in performance/src/gatling/simulations/
ALWAYS document CDN limitation in README
ALWAYS run mvn gatling:test locally before pushing
```

---

## 3. Tech Stack

| Layer | Technology | Version | Why |
|---|---|---|---|
| Load tool | Gatling | 3.11+ | Java DSL, Maven plugin, enterprise HTML report |
| Language | Java | 17 LTS | Same as parent project |
| Build | Maven Gatling Plugin | 4.x | `mvn gatling:test` — fits existing Maven workflow |
| Report | Gatling HTML Report | built-in | Best-in-class, interactive, zero config |
| CI | GitHub Actions | current | Existing pipeline — add one job |

---

## 4. Maven Configuration — Add to pom.xml

Add a profile to the **existing** `pom.xml` — do not create a separate module.

```xml
<!-- Add inside <profiles> section of existing pom.xml -->
<profile>
  <id>performance</id>
  <dependencies>
    <dependency>
      <groupId>io.gatling.highcharts</groupId>
      <artifactId>gatling-charts-highcharts</artifactId>
      <version>3.11.5</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
        <groupId>io.gatling</groupId>
        <artifactId>gatling-maven-plugin</artifactId>
        <version>4.9.6</version>
        <configuration>
          <simulationsFolder>performance/src/gatling/simulations</simulationsFolder>
          <resultsFolder>performance/target/gatling</resultsFolder>
          <!-- Run only in this profile — never during mvn test -->
          <runMultipleSimulations>false</runMultipleSimulations>
        </configuration>
      </plugin>
    </plugins>
    <testSourceDirectory>performance/src/gatling/simulations</testSourceDirectory>
  </build>
</profile>
```

**Important:** The `performance` profile is never activated by default.
`mvn test` runs TestNG only. `mvn gatling:test -Pperformance` runs Gatling only.

---

## 5. Directory Structure

```
qa-lab-selenium-java/       ← existing repo root
└── performance/
    └── src/
        └── gatling/
            └── simulations/
                ├── QALabSmokeSimulation.java
                ├── QALabBaselineSimulation.java
                └── QALabColdWarmSimulation.java
```

---

## 6. Simulations — Exact Implementation

### QALabSmokeSimulation.java
```java
// performance/src/gatling/simulations/QALabSmokeSimulation.java
package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class QALabSmokeSimulation extends Simulation {

    private static final String BASE_URL =
        System.getenv("BASE_URL") != null
            ? System.getenv("BASE_URL")
            : "https://subbotin.es";

    HttpProtocolBuilder httpProtocol = http
        .baseUrl(BASE_URL)
        .acceptHeader("text/html,application/xhtml+xml")
        .userAgentHeader("Gatling/3.x — QA Lab Performance Suite");

    ScenarioBuilder smoke = scenario("QA Lab Smoke")
        .exec(
            http("QA Lab page")
                .get("/QA-Lab/qa-lab.html")
                .check(status().is(200))
                .check(bodyString().contains("QA Lab"))
        )
        .pause(1);

    {
        setUp(
            smoke.injectOpen(
                rampUsers(5).during(10),   // ramp to 5 VU over 10s
                constantUsersPerSec(5).during(20)  // hold 20s
            )
        )
        .protocols(httpProtocol)
        .assertions(
            global().responseTime().percentile(95).lt(500),
            global().responseTime().percentile(99).lt(1000),
            global().failedRequests().percent().lt(1.0)
        );
    }
}
```

### QALabBaselineSimulation.java
```java
// performance/src/gatling/simulations/QALabBaselineSimulation.java
package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class QALabBaselineSimulation extends Simulation {

    private static final String BASE_URL =
        System.getenv("BASE_URL") != null
            ? System.getenv("BASE_URL")
            : "https://subbotin.es";

    HttpProtocolBuilder httpProtocol = http
        .baseUrl(BASE_URL)
        .acceptHeader("text/html");

    ScenarioBuilder baseline = scenario("QA Lab Baseline")
        .exec(
            http("QA Lab main page")
                .get("/QA-Lab/qa-lab.html")
                .check(status().is(200))
        )
        .pause(1)
        .exec(
            http("QA Lab index")
                .get("/QA-Lab/index.html")
                .check(status().is(200))
        )
        .pause(1);

    {
        setUp(
            baseline.injectOpen(
                rampUsers(10).during(15),
                constantUsersPerSec(10).during(45)
            )
        )
        .protocols(httpProtocol)
        .assertions(
            global().responseTime().percentile(95).lt(500),
            global().responseTime().percentile(99).lt(1000),
            global().failedRequests().percent().lt(1.0),
            global().successfulRequests().percent().gt(99.0)
        );
    }
}
```

### QALabColdWarmSimulation.java
```java
// performance/src/gatling/simulations/QALabColdWarmSimulation.java
package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * Single-user sequential cold/warm CDN comparison.
 * Tags requests separately so Gatling report shows both distributions.
 */
public class QALabColdWarmSimulation extends Simulation {

    private static final String BASE_URL =
        System.getenv("BASE_URL") != null
            ? System.getenv("BASE_URL")
            : "https://subbotin.es";

    HttpProtocolBuilder httpProtocol = http.baseUrl(BASE_URL);

    ScenarioBuilder coldWarm = scenario("CDN Cold vs Warm")
        .exec(
            http("cold_hit")   // First request — potentially cold
                .get("/QA-Lab/qa-lab.html")
                .check(status().is(200))
        )
        .exec(
            http("warm_hit")   // Second request — warm cache
                .get("/QA-Lab/qa-lab.html")
                .check(status().is(200))
        );

    {
        setUp(
            coldWarm.injectOpen(atOnceUsers(1)).protocols(httpProtocol)
        )
        .assertions(
            details("cold_hit").responseTime().percentile(95).lt(1500),
            details("warm_hit").responseTime().percentile(95).lt(200)
        );
    }
}
```

---

## 7. CI Integration — Add to Existing Pipeline

```yaml
  performance:
    name: Gatling SLO Check
    runs-on: ubuntu-latest
    needs: test          # run after TestNG passes
    timeout-minutes: 15

    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: 'maven'

      - name: Run Gatling smoke simulation
        run: |
          mvn gatling:test -Pperformance \
            -Dgatling.simulationClass=simulations.QALabSmokeSimulation
        env:
          BASE_URL: https://subbotin.es

      - name: Run Gatling baseline simulation (main only)
        if: github.ref == 'refs/heads/main'
        run: |
          mvn gatling:test -Pperformance \
            -Dgatling.simulationClass=simulations.QALabBaselineSimulation
        env:
          BASE_URL: https://subbotin.es

      - name: Upload Gatling HTML report
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: gatling-report
          path: performance/target/gatling/
          retention-days: 14
```

---

## 8. Infrastructure Setup — Step by Step

### Step 1: Add profile to pom.xml

Per Section 4 — add `<profile id="performance">` inside existing `<profiles>` block.
If `<profiles>` doesn't exist in pom.xml, add it before `</project>`.

### Step 2: Create directory structure

```bash
mkdir -p performance/src/gatling/simulations
```

### Step 3: Create simulations per Section 6

### Step 4: Verify compilation

```bash
mvn compile -Pperformance
```

### Step 5: Run locally

```bash
# Smoke simulation
mvn gatling:test -Pperformance \
  -Dgatling.simulationClass=simulations.QALabSmokeSimulation

# Report opens automatically in browser
# Or find it at: performance/target/gatling/<timestamp>/index.html
```

---

## 9. Definition of Done

```
□ mvn compile -Pperformance — zero errors
□ All 3 simulations compile and run locally
□ Assertions pass locally (p95 < 500ms on warm CDN)
□ performance/ README.md written with honest scope
□ CI job added — does not activate on mvn test (profile isolation)
□ Gatling HTML report uploaded as artifact in CI
□ Commit message: perf(gatling): add SLO compliance simulations for QA Lab CDN
```

---

*End of CLAUDE.md*
*Version: 1.0 | Author: Evgenii Subbotin | Augmentation: Gatling → qa-lab-selenium-java*
*May 2026*
