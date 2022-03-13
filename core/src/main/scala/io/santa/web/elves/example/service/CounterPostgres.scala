package io.santa.web.elves.example.service

import io.getquill._
import io.santa.web.elves.example.service.CounterPostgres.{
  counterKey,
  CounterData,
  QuillContext
}
import zio._

import javax.sql.DataSource

case class CounterPostgres(dataSource: DataSource) extends Counter {
  import QuillContext._
  private val dataSourceEnv = Has(dataSource)

  private val getCounterQuery = quote {
    query[CounterData].filter(c => c.key == counterKey).map(_.value)
  }

  override def getCounter: Task[Int] =
    for {
      maybeCounter <- run(getCounterQuery)
        .provide(dataSourceEnv)
        .map(_.headOption)
      counter <- Task.getOrFail(maybeCounter)
    } yield counter

  // TODO: better use raw sql here to increment counter
  private def incrementCounterQuery(i: Int) = transaction {
    for {
      counter <- getCounter
      updatedCounter = counter + i
      updateCounterQuery = quote {
        query[CounterData]
          .filter(_.key == counterKey)
          .updateValue(lift(CounterData(counterKey, updatedCounter)))
      }
      _ <- run(updateCounterQuery)
    } yield ()
  }

  override def incrementCounter(i: Int): Task[Unit] =
    incrementCounterQuery(i).provide(dataSourceEnv).unit

}

object CounterPostgres {

  val layer: RLayer[Has[DataSource], Has[Counter]] =
    (CounterPostgres(_)).toLayer

  object QuillContext extends PostgresZioJdbcContext(SnakeCase)

  private case class CounterData(key: String, value: Int)

  final private val counterKey = "counter"
}
