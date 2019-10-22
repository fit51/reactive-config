package com.github.fit51.reactiveconfig.examples

import cats.effect.{Async, ContextShift}
import com.github.fit51.reactiveconfig.etcd.{ChannelManager, EtcdClient}
import monix.eval.Task
import monix.execution.Scheduler
import cats.syntax.flatMap._
import cats.syntax.functor._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import io.circe.generic.auto._

object EtcdConfigMutateConfig extends App {
  implicit val scheduler = Scheduler.global

  Await.result(FillConfig.fill[Task].runToFuture, Duration.Inf)
}

object FillConfig {
  import StoreModule._
  // Initial store is hardcoded
  val store: Map[ProductId, Count] = Map(
    "ProgrammingInScala"     -> 4,
    "FuncProgrammingInScala" -> 0,
    "ScalaPuzzlers"          -> 15,
    "ScalaInDepth"           -> 1
  )
  private val priceList: Map[ProductId, Money] = Map(
    "ProgrammingInScala"     -> 100.20,
    "FuncProgrammingInScala" -> 50.25,
    "ScalaPuzzlers"          -> 1.50,
    "ScalaInDepth"           -> 1.0
  )
  private val adverts = List("ScalaInDepth")

  import io.circe.syntax._

  def fill[F[_]: ContextShift: Async](implicit scheduler: Scheduler): F[Unit] = {
    val F = implicitly[Async[F]]
    for {
      client <- F.pure(new EtcdClient[F](ChannelManager.noAuth("http://127.0.0.1:2379")))
      storeConfig = StoreConfig(priceList, "1")
      _ <- client.put("store.store", storeConfig.asJson.noSpaces)
      _ <- client.put("store.adverts", adverts.asJson.noSpaces)
      _ <- F.delay(client.close())
      _ <- F.delay(println("Finish!"))
    } yield ()
  }
}
