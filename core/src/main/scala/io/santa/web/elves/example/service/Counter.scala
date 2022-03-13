package io.santa.web.elves.example.service

import zio.{Has, RIO, Task}

trait Counter {
  def getCounter: Task[Int]

  def incrementCounter(i: Int): Task[Unit]
}

object Counter {
  def getCounter: RIO[Has[Counter], Int] = RIO.serviceWith(_.getCounter)

  def incrementCounter(i: Int): RIO[Has[Counter], Unit] =
    RIO.serviceWith(_.incrementCounter(i))

}
