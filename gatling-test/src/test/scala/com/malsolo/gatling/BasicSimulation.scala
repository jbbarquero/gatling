package com.malsolo.gatling

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class BasicSimulation extends Simulation {

  val httpProtocol = http
    .baseURL("http://computer-database.gatling.io")
    .inferHtmlResources(BlackList(""".*\.css, .*\.js and .*\.ico"""), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.9,es-ES;q=0.8,es;q=0.7")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")

  val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

  val headers_5 = Map(
    "Origin" -> "http://computer-database.gatling.io",
    "Upgrade-Insecure-Requests" -> "1")



  val scn = scenario("BasicSimulation")
    // Search
    .exec(http("request_0")
    .get("/")
    .headers(headers_0))
    .pause(13)
    .exec(http("request_1")
      .get("/computers?f=macbook")
      .headers(headers_0))
    .pause(4)
    .exec(http("request_2")
      .get("/computers/89")
      .headers(headers_0))
    .pause(3)
    .exec(http("request_3")
      .get("/computers")
      .headers(headers_0))
    .pause(5)
    .exec(http("request_4")
      .get("/computers/new")
      .headers(headers_0))
    .pause(22)
    .exec(http("request_5")
      .post("/computers")
      .headers(headers_5)
      .formParam("name", "MacDouglas")
      .formParam("introduced", "2018-02-01")
      .formParam("discontinued", "")
      .formParam("company", "1"))

  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}