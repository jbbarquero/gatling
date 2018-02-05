package com.malsolo.gatling

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._


class AdvancedSimulationStep00 extends Simulation {

  object Search {

    val search = exec(http("Home")
      .get("/"))
      .pause(1)
      .exec(http("Search")
        .get("/computers?f=macbook"))
      .pause(1)
      .exec(http("Select")
        .get("/computers/6"))
      .pause(1)

  }

  object Browse {

    /*
    def gotoPage(page: Int) = exec(http("Page " + page)
      .get("/computers?p=" + page))
      .pause(1)

    val browse = exec(gotoPage(0), gotoPage(1), gotoPage(2), gotoPage(3), gotoPage(4))
    */
    val browse = repeat(5, "n") { // 1
      exec(http("Page ${n}")
        .get("/computers?p=${n}")) // 2
        .pause(1)
    }
  }

  object Edit {

    val headers_10 = Map("Content-Type" -> "application/x-www-form-urlencoded")

    val edit = exec(http("Form")
      .get("/computers/new"))
      .pause(1)
      .exec(http("Post")
        .post("/computers")
        .headers(headers_10)
        .formParam("name", "Beautiful Computer")
        .formParam("introduced", "2012-05-30")
        .formParam("discontinued", "")
        .formParam("company", "37"))
  }
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

  val scn = scenario("AdvancedSimulation").exec(Search.search, Browse.browse, Edit.edit)

  val users = scenario("Users").exec(Search.search, Browse.browse)
  val admins = scenario("Admins").exec(Search.search, Browse.browse, Edit.edit)

  //setUp(scn.inject(atOnceUsers(10)).protocols(httpProtocol))

  setUp(
    users.inject(rampUsers(10) over (10 seconds)),
    admins.inject(rampUsers(2) over (10 seconds))
  ).protocols(httpProtocol)

}
