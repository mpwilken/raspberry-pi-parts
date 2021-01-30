package us.thirdbase.requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import us.thirdbase.config.Config.{app_url, authToken}

object LoginRequest {

    //    val sentHeaders = Map("Authorization" -> "bearer ${token}")

    val login =
        exec(http("Login Request")
            .get(app_url + "/login")
            .basicAuth("joe", "password")
            .check(status is 200)
            .check(header(authToken).saveAs("token")))
}
