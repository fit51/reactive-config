package com.github.fit51.reactiveconfig.examples

import cats.effect.{ContextShift, Sync}
import cats.effect.IO
import cats.effect.Timer
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.github.fit51.reactiveconfig.ce.etcd._
import com.github.fit51.reactiveconfig.etcd._
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object EtcdConfigMutateConfig extends App {
  implicit val ioTimer: Timer[IO]               = IO.timer(global)
  implicit val ioContextShift: ContextShift[IO] = IO.contextShift(global)

  val channel = ChannelManager.noAuth("127.0.0.1:2379", options = ChannelOptions(20 seconds)).channel
  val client  = EtcdClient[IO](channel)
  FillConfig.fill(client).unsafeRunSync()
  channel.shutdown()
}

object FillConfig {
  import StoreModule._
  import io.circe.syntax._
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

  def fill[F[_]](client: EtcdClient[F])(implicit F: Sync[F]): F[Unit] =
    for {
      storeConfig <- F.pure(StoreConfig(priceList, "1"))
      _           <- client.put("store.store", storeConfig.asJson.noSpaces)
      _           <- client.put("store.adverts", adverts.asJson.noSpaces)
      _           <- F.delay(println("Finish!"))
    } yield ()
}
