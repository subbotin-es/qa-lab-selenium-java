# Performance Testing Findings — Gatling + Java DSL

**Project:** QA Lab Cross-Stack Series — Stack 3: Selenium + Java + TestNG
**Augmentation:** Gatling 3.11 embedded as Maven profile
**Target:** https://subbotin.es/QA-Lab/qa-lab.html
**Author:** Evgenii Subbotin
**Date:** May 2026

---

## 1. Approach

### Why Gatling in this repo

Gatling 3.x with the Java DSL is the natural performance companion to a Maven + Java project.
The same build tool, the same language, the same CI pipeline — no toolchain shift.
In financial services and enterprise IT (the primary audience for this stack), Gatling is
the recognised Java-ecosystem performance tool that fits without a migration conversation.

The alternative — adding a separate JMeter or k6 job — would introduce a second language
or a second build system. Gatling compiles as a standard Maven test source and runs via
`mvn gatling:test -Pperformance`, consistent with how the team already runs `mvn test`.

### Profile isolation

Gatling lives in a `performance` Maven profile. `mvn test` runs TestNG only.
`mvn gatling:test -Pperformance` runs Gatling only. The two test types never interfere.

The `build-helper-maven-plugin` registers `performance/src/gatling/simulations/` as a
test source root only when the profile is active. The Gatling dependency
(`gatling-charts-highcharts`) is also scoped to the profile and never contaminates the
functional test classpath.

---

## 2. Target Characteristics

| Property | Value |
|---|---|
| URL | https://subbotin.es/QA-Lab/qa-lab.html |
| Hosting | AWS S3 static website |
| CDN | AWS CloudFront (global edge network) |
| Content type | Static HTML + vanilla JavaScript (~50 KB total) |
| Origin location | eu-west-1 (inferred from latency profile) |
| CI runner location | GitHub Actions `ubuntu-latest` — AWS us-east-1 or us-east-2 |

Because the target is a **CDN-served static page**, all performance measurements reflect
CloudFront edge cache behaviour, not application-tier or database performance.
The page content does not change between test runs. Every request should be a cache hit
at the nearest CloudFront edge node.

---

## 3. Simulations

### QALabSmokeSimulation

**Purpose:** Fast SLO gate — confirms the page is available and within latency thresholds
before a deployment or as a recurring health check.

**Load profile:**
- Ramp 5 virtual users over 10 seconds
- Hold 5 users/second for 20 seconds
- Total requests: ~150

**Scenario:** Single GET `/QA-Lab/qa-lab.html` with body content check (`substring("QA Lab")`)
and HTTP 200 assertion.

**SLO assertions:**
- p95 response time < 500 ms
- p99 response time < 1 000 ms
- Error rate < 1%

**Runs in CI:** every push and pull request (after TestNG passes).

---

### QALabBaselineSimulation

**Purpose:** Sustained load baseline — establishes the CDN's steady-state performance
profile under moderate concurrent traffic. Serves as the reference for future comparisons.

**Load profile:**
- Ramp 10 virtual users over 15 seconds
- Hold 10 users/second for 45 seconds
- Total virtual users: ~460; total requests: ~920

**Scenario:** Two sequential GETs to `/QA-Lab/qa-lab.html` with a 2-second pause between
them (first load → repeat load), simulating a user browsing and refreshing.

**SLO assertions:**
- p95 response time < 500 ms
- p99 response time < 1 000 ms
- Error rate < 1%
- Success rate > 99%

**Runs in CI:** `main` branch only (after smoke passes).

---

### QALabColdWarmSimulation

**Purpose:** CDN cache state comparison — demonstrates the latency difference between
a potentially cold CDN edge (first request) and a confirmed warm CDN edge (immediate
repeat request). Not in CI; available for local exploratory runs.

**Load profile:** 1 virtual user, 2 sequential requests (no pause).

**Scenario:** `cold_hit` → `warm_hit`, both to `/QA-Lab/qa-lab.html`.
Requests are tagged separately so the Gatling HTML report shows two distinct distributions.

**SLO assertions:**
- `cold_hit` p95 < 1 500 ms (allows for edge cache miss / origin fetch)
- `warm_hit` p95 < 200 ms (confirmed cache hit at edge)

**Note:** With a fully-warm CDN, the cold_hit is also served from edge cache and
the two response times converge. The simulation is most informative immediately after
a CloudFront cache invalidation or on a freshly deployed region.

---

## 4. Observed Results

Results from green CI runs on GitHub Actions `ubuntu-latest` (AWS us-east-1):

| Metric | Smoke | Baseline (first load) | Baseline (repeat load) |
|---|---|---|---|
| p75 | ~15 ms | ~24 ms | ~18 ms |
| p95 | ~20 ms | ~27 ms | ~22 ms |
| p99 | ~25 ms | ~32 ms | ~28 ms |
| Mean | ~12 ms | ~14 ms | ~13 ms |
| Error rate | < 1% | < 1% | < 1% |
| SLOs passed | All | All | All |

All response times are consistent with **CloudFront edge-cache hits**. The CDN fully
absorbs the test load with no measurable degradation across concurrent users.

---

## 5. Technical Findings from Debugging

Three distinct failure modes were encountered and resolved during CI stabilisation.
These are documented here as they illustrate real-world CDN and tooling behaviour.

### 5.1 HTTP 403 Forbidden — CloudFront WAF block

**Symptom:** 100% of requests returned 403 with ~13 ms response time.

**Root cause:** Gatling's default HTTP client did not set a `User-Agent` header.
The CloudFront distribution's WAF rules blocked requests without a recognisable
user agent string.

**Fix:** Added `.userAgentHeader("Gatling/3.x — QA Lab Performance Suite")` to all
simulation HTTP protocol builders.

**Lesson:** CloudFront WAF can silently block load test traffic based on missing or
bot-classified User-Agent values. Always set an explicit user agent.

---

### 5.2 HTTP 304 Not Modified — Gatling internal HTTP cache

**Symptom:** ~50% of requests returned 304, causing `status().is(200)` assertions to fail.

**Root cause:** Gatling 3.x enables an RFC 7234 HTTP cache by default. When a user session
made two requests to the same URL, Gatling cached the first response (including `ETag`
and `Last-Modified` headers from CloudFront) and sent a conditional GET for the second
request. CloudFront correctly returned 304. The `status().is(200)` check then failed.

**Fix:** Added `.disableCaching()` to all HTTP protocol builders.
This prevents Gatling from caching responses or sending conditional GET headers,
ensuring every request is a fresh cache-hit against CloudFront.

**Why this matters:** In a load test, simulating thousands of independent users — not a
single browser with a local cache. `.disableCaching()` is the correct default for load
testing scenarios. The 304 behaviour is valid for browser testing but misleading here.

**Additional note for QALabColdWarmSimulation:** Status check was widened to
`status().in(200, 304)` as a safety net, since that simulation explicitly tests caching
dynamics and both codes represent valid CDN responses.

---

### 5.3 Missing `/QA-Lab/index.html` — 404 cascade failure

**Symptom:** 100% request failure rate in the baseline simulation.

**Root cause:** The original baseline scenario included a second request to
`/QA-Lab/index.html` which does not exist on the target deployment.
CloudFront returned 404. Because Gatling aborts a virtual user's session on a failed
check, and the first request in some users also failed due to the WAF issue (5.1),
the combined failure rate appeared as 100%.

**Fix:** Replaced `/QA-Lab/index.html` with a second request to `/QA-Lab/qa-lab.html`,
converting the scenario into a first-load → repeat-load pattern. This is more semantically
meaningful for CDN testing and avoids relying on untested URL paths.

---

## 6. Assumptions

| Assumption | Basis |
|---|---|
| CloudFront cache is warm at test start | The page has been live and accessed regularly |
| CI runner is in AWS us-east-1 or us-east-2 | GitHub Actions `ubuntu-latest` placement |
| Target is not rate-limited at tested load | 10 req/s is well below CloudFront default limits |
| Page content is static and does not change during test | S3 static hosting — no server-side rendering |
| p95 SLO of 500 ms is appropriate | Industry standard for web pages; CDN target should far exceed it |

---

## 7. Limitations

| Limitation | Detail |
|---|---|
| No capacity ceiling | The CDN target does not degrade under the tested load. True capacity testing is not applicable to a static S3 + CloudFront deployment. |
| No degradation curve | Response times are flat across all user counts. There is no inflection point to find. |
| CI runner proximity | GitHub Actions runners are geographically near AWS edge nodes. Response times from real end users in Europe or Asia Pacific would differ. |
| Single endpoint | All simulations target the same HTML page. A real application would require multi-endpoint scenarios covering authenticated flows, API calls, and asset loading. |
| No think-time variation | Pause durations are fixed constants. Real user think-time follows a distribution; `pause(minDuration, maxDuration)` would be more realistic. |
| No ramp-down | Simulations end abruptly. A ramp-down phase would give a cleaner report tail. |
| HTTP/1.1 only | The Gatling HTTP protocol builder defaults to HTTP/1.1 unless `http2.enable()` is configured. CloudFront supports HTTP/2 and HTTP/3; measured latencies may be slightly higher than a real browser which negotiates a better protocol. |

---

## 8. Portfolio Narrative

> "Performance testing lives in the Java repo because the team that owns Maven owns Gatling.
> Same build tool, same language, same CI. In financial services and enterprise IT —
> exactly where this stack is positioned — Gatling is the recognised performance tool
> that fits without a migration conversation.
>
> The findings document what the tests actually measure (CDN edge-cache delivery),
> what they cannot measure (application-tier capacity), and the three failure modes
> discovered during CI stabilisation. Honest scope documentation is as important as
> the green badge."

---

## 9. Useful Links

| Resource | URL |
|---|---|
| Gatling Java DSL documentation | https://docs.gatling.io/reference/script/core/simulation/ |
| Gatling Maven Plugin 4.x reference | https://docs.gatling.io/reference/integrations/build-tools/maven-plugin/ |
| Gatling assertions API | https://docs.gatling.io/reference/script/core/assertions/ |
| Gatling injection profiles | https://docs.gatling.io/reference/script/core/injection/ |
| CloudFront cache behaviour | https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/cache-hit-ratio.html |
| build-helper-maven-plugin | https://www.mojohaus.org/build-helper-maven-plugin/add-test-source-mojo.html |
| GitHub Actions CI run | https://github.com/subbotin-es/qa-lab-selenium-java/actions |
| Gatling HTML report artifact | Download from Actions → workflow run → Artifacts → `gatling-report` |

---

*End of Findings*
*Author: Evgenii Subbotin | May 2026*
