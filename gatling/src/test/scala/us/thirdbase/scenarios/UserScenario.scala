package us.thirdbase.scenarios

import io.gatling.core.Predef.{jsonFile, scenario, _}
import io.gatling.core.structure.ScenarioBuilder
import us.thirdbase.requests.{CreatePartRequest, LoginRequest, PageRequest}

object UserScenario {
    val feeder = jsonFile("page-and-size.json").circular

    val userScenario: ScenarioBuilder = scenario("User Scenario")
        //        .feed(feeder)
        .exec(LoginRequest.login)
        .exec(PageRequest.viewPages)
        .exec(CreatePartRequest.createPart)
}
