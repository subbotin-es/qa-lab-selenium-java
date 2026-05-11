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
        .disableCaching()
        .acceptHeader("text/html,application/xhtml+xml")
        .userAgentHeader("Gatling/3.x — QA Lab Performance Suite");

    ScenarioBuilder baseline = scenario("QA Lab Baseline")
        .exec(
            http("QA Lab page - first load")
                .get("/QA-Lab/qa-lab.html")
                .check(status().is(200))
        )
        .pause(2)
        .exec(
            http("QA Lab page - repeat load")
                .get("/QA-Lab/qa-lab.html")
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
