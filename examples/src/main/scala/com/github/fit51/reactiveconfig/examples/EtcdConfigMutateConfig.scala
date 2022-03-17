package com.github.fit51.reactiveconfig.examples

import cats.effect.{Async, ContextShift}
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.github.fit51.reactiveconfig.etcd.{ChannelManager, EtcdClient}
import io.circe.generic.auto._
import monix.eval.Task
import monix.execution.Scheduler

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object EtcdConfigMutateConfig extends App {
  implicit val scheduler = Scheduler.global
  val chManager          = ChannelManager.noAuth("127.0.0.1:2379")
  val client             = new EtcdClient[Task](chManager)
  Await.result(FillConfig.fill[Task](client).runToFuture, Duration.Inf)
  client.close()
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

  def fill[F[_]: ContextShift: Async](client: EtcdClient[F])(implicit scheduler: Scheduler): F[Unit] = {
    val F = implicitly[Async[F]]
    for {
      storeConfig <- F.pure(StoreConfig(priceList, "1"))
      _           <- client.put("store.store", storeConfig.asJson.noSpaces)
      _           <- client.put("store.adverts", adverts.asJson.noSpaces)
      _           <- F.delay(println("Finish!"))
    } yield ()
  }
}
