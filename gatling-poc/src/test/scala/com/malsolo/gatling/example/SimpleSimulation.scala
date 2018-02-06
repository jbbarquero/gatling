package com.malsolo.gatling.example

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class SimpleSimulation extends Simulation {

  object Health {
    val health = exec(http("Check Health").
      get("/actuator/health")
      .check(status.is(200)))
      .pause(1)
  }

  object Create {
    val create = exec(http("Create status")
      .post("/status").body(StringBody(
      """{
        |    "name": "Gemma",
        |    "description": "her",
        |    "status": "true",
        |    "amount": "2.0"
        |}""".stripMargin))
      .check(status.is(200))
      .check(jsonPath("$.id").saveAs("statusId")))
      .pause(1)
  }

  object Read {
    val read = exec(http("Ask for status")
      .get("/status/${statusId}")
      .check(status.is(200))
      .check(jsonPath("$..id").is("${statusId}")))
      .pause(1)
  }

  private val httpConfig = http.baseURL("http://localhost:8080")
    .contentTypeHeader("application/json")

  private val scn = scenario("Status scenario").
    exec(Health.health, Create.create, Read.read)

  setUp(
    scn.inject(rampUsers(10) over (10 seconds))
  ).protocols(httpConfig)
    .assertions(
      global.successfulRequests.percent.gt(95),
      global.responseTime.max.lt(80),
      global.responseTime.mean.lt(30),
      global.responseTime.percentile1.lt(20)
    )

}
