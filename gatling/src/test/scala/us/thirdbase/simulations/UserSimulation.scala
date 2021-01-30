package us.thirdbase.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import us.thirdbase.config.Config._
import us.thirdbase.scenarios.UserScenario

import scala.concurrent.duration._ // in order to get minutes, seconds

class UserSimulation extends Simulation {

    val httpProtocol: HttpProtocolBuilder = http
        .baseUrl(app_url)
        .inferHtmlResources()
        .acceptHeader("application/json, text/plain, */*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("en-US,en;q=0.9")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")

    private val userExec = UserScenario.userScenario
        .inject(
            nothingFor(4 seconds),
            atOnceUsers(10),
            rampUsers(10) during (5 seconds),
            constantUsersPerSec(20) during (15 seconds),
            constantUsersPerSec(20) during (15 seconds) randomized,
            rampUsersPerSec(10) to 20 during (5 minutes),
            rampUsersPerSec(10) to 20 during (5 minutes) randomized,
            heavisideUsers(1000) during (20 seconds)
        ).protocols(httpProtocol)

    setUp(userExec)

}
