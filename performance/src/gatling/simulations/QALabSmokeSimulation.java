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
                rampUsers(5).during(10),
                constantUsersPerSec(5).during(20)
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
