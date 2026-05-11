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
            http("cold_hit")
                .get("/QA-Lab/qa-lab.html")
                .check(status().is(200))
        )
        .exec(
            http("warm_hit")
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
