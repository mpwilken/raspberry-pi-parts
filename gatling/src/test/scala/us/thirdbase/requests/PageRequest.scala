package us.thirdbase.requests

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import us.thirdbase.config.Config.{app_url, authToken}

object PageRequest {

    val viewPages: ChainBuilder =
        repeat(8, "n") {
            exec(_.set("pageSize", 7))
                .exec { session => // pick a random page size between 3 and 7
                    val random = scala.util.Random
                    val start = 3
                    val end = 7
                    val randomPageSize = start + random.nextInt( (end - start) + 1 )
                    session.set("pageSize", randomPageSize)
                }
//                .exec {session => // output values in session if you want to debug
//                    println(session)
//                    session
//                }
                .exec(http("Next page")
                    .get(app_url + "/api/parts/page")
                    .queryParam("page", "${n}")
                    .queryParam("size", "${pageSize}")
                    .header(authToken, "${token}")
                    .check(status is 200))
                .pause(1)
        }
}
