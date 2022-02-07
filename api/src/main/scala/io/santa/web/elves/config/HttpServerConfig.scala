package io.santa.web.elves.config

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.string.IPv4

final case class HttpServerConfig(
  bindAddress: String Refined IPv4,
  port: Int Refined Positive
)
