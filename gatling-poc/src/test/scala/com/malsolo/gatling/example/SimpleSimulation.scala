package com.malsolo.gatling.example

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class SimpleSimulation extends Simulation {

  private val httpConfig = http.baseURL("http://computer-database.gatling.io")

  private val scn = scenario("SimpleSimulation").
    exec(http("open").
      get("/")).
    pause(1)

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConfig)
}
