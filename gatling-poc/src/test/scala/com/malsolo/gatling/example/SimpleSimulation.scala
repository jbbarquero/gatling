package com.malsolo.gatling.example

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class SimpleSimulation extends Simulation {

  private val httpConfig = http.baseURL("http://localhost:8080")
    .contentTypeHeader("application/json")

  private val scn = scenario("SimpleSimulation").
    exec(http("Check Health").
      get("/actuator/health")
      .check(status.is(200)))
    .pause(1)
    .exec(http("Create status")
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
    .exec(http("Ask for status")
      .get("/status/${statusId}")
      .check(status.is(200))
      .check(jsonPath("$..id").is("${statusId}")))
    .pause(1)

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConfig)
}
