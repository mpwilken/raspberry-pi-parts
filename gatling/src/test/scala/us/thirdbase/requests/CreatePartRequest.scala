package us.thirdbase.requests

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import us.thirdbase.config.Config.authToken

object CreatePartRequest {

    val partsFeeder = jsonFile("sample-parts.json").circular
    val createPart: ChainBuilder =
    //        exec { session =>
    //            println(session.get("part"))
    //            session
    //        }
        feed(partsFeeder)
            .exec(
                http("Create part")
                    .post("/api/parts/")
                    .headers(Map("Content-Type" -> "application/json"))
                    .body(StringBody("""{"name": "${name}", "cost": "${cost}", "quantity": "${quantity}", "description": "${description}", "url": "${url}"}""".stripMargin))
                    .header(authToken, "${token}")
                    .check(status is 201)
                //                .check(jsonPath("$..someField").is("some value"))
            )
            .pause(1)
}
