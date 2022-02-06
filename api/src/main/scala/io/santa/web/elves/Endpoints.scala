package io.santa.web.elves

import sttp.model.StatusCode
import sttp.tapir.PublicEndpoint
import sttp.tapir.ztapir._

object Endpoints {

  val getCounterEndpoint: PublicEndpoint[Unit, StatusCode, (Int, StatusCode), Any] =
    endpoint
      .get
      .name("Get counter")
      .description("Returns current value of the counter")
      .in("counter")
      .out(plainBody[Int])
      .out(
        statusCode
          .description(
            StatusCode.Ok,
            """Value of the counter is successfully returned"""
          )
      )
      .errorOut(
        statusCode.description(
          StatusCode.InternalServerError,
          """Error interacting with database"""
        )
      )

  val incrementCounterEndpoint: PublicEndpoint[Int, StatusCode, StatusCode, Any] =
    endpoint
      .post
      .name("Increment counter")
      .description("Increments the counter by the specified amount")
      .in("counter")
      .in(plainBody[Int])
      .out(
        statusCode
          .description(
            StatusCode.Ok,
            """The value of the counter has successfully been incremented"""
          )
      )
      .errorOut(
        statusCode.description(
          StatusCode.InternalServerError,
          """Error interacting with database"""
        )
      )

}
