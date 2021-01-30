package us.thirdbase.config

object Config {
    val app_url = "http://parts:8080"

    val users = Integer.getInteger("users", 30).toInt
    val pageSize = Integer.getInteger("pageSize", 7).toInt
    val rampUp = Integer.getInteger("rampup", 1).toInt
    val throughput = Integer.getInteger("throughput", 100).toInt
    val authToken = "Authorization"
}
