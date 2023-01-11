package com.github.fit51.reactiveconfig.ce.etcd

import cats.FlatMap
import cats.Parallel
import cats.data.NonEmptySet
import cats.effect.Async
import cats.effect.ConcurrentEffect
import cats.effect.ContextShift
import cats.effect.Resource
import cats.effect.Timer
import cats.effect.concurrent.Ref
import cats.effect.concurrent.Semaphore
import cats.effect.syntax.concurrent._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.parallel._
import com.github.fit51.reactiveconfig.Value
import com.github.fit51.reactiveconfig.ce.config.AbstractReactiveConfig
import com.github.fit51.reactiveconfig.ce.config.ReactiveConfig
import com.github.fit51.reactiveconfig.config.ConfigState
import com.github.fit51.reactiveconfig.etcd._
import com.github.fit51.reactiveconfig.etcd.gen.kv.Event
import com.github.fit51.reactiveconfig.etcd.gen.rpc.KVFs2Grpc
import com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeRequest
import com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchCreateRequest
import com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchFs2Grpc
import com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchRequest
import com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchResponse
import com.github.fit51.reactiveconfig.parser.ConfigParser
import com.github.fit51.reactiveconfig.typeclasses.Effect
import io.grpc.Channel
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusRuntimeException

import scala.concurrent.duration._
import scala.util.{Failure, Success}

object EtcdReactiveConfig {

  def apply[F[_]: ConcurrentEffect: Parallel: ContextShift: Timer, D: ConfigParser](
      channel: Channel,
      prefixes: NonEmptySet[String],
      retry: FiniteDuration = 20.seconds
  ): Resource[F, ReactiveConfig[F, D]] = {
    import com.github.fit51.reactiveconfig.ce.reloadable.Reloadable._

    val kvClient    = KVFs2Grpc.stub[F](channel)
    val watchClient = WatchFs2Grpc.stub[F](channel)

    for {
      stateRef <- Resource.eval(
        loadInitials[F, D](prefixes, kvClient).map(ConfigState[F, D](_, Map.empty)) >>= Ref[F].of
      )
      semaphore <- Resource.eval(Semaphore(1))
      _ <- Resource.make(
        prefixes
          .foldLeft[fs2.Stream[F, WatchResponse]](fs2.Stream.empty) { case (acc, prefix) =>
            acc.merge(watchKey(watchClient, prefix, retry))
          }.evalMap(handleUpdate(_, stateRef, semaphore)).compile.last.start
      )(_.cancel)
    } yield new AbstractReactiveConfig[F, D](stateRef, semaphore)
  }

  private def loadInitials[F[_]: Parallel: ContextShift, D](
      prefixes: NonEmptySet[String],
      kvClient: KVFs2Grpc[F, Metadata]
  )(implicit decoder: ConfigParser[D], F: Async[F], effect: Effect[F]): F[Map[String, Value[D]]] =
    prefixes.toNonEmptyList.toList.toVector.parTraverse { prefix =>
      val keyRange = prefix.asKeyRange
      kvClient
        .range(RangeRequest(key = keyRange.start.bytes, rangeEnd = keyRange.end.bytes), new Metadata())
        .map(_.kvs.toVector)
    }.map(_.flatten).map { allKvs =>
      allKvs.partitionMap { kv =>
        decoder.parse(kv.value.utf8) match {
          case Success(parsed) =>
            val key   = kv.key.utf8
            val value = Value(parsed, kv.version)
            Right(key -> value)
          case Failure(exception) =>
            Left(kv.key.utf8 -> exception.getMessage())
        }
      }
    } flatMap { case (errors, parsedKvs) =>
      import com.github.fit51.reactiveconfig.typeclasses.Effect._

      Effect
        .traverse(errors) { case (key, message) =>
          effect.warn(s"Unable to parse key $key: $message")
        }.as(parsedKvs.toMap)
    }

  private def watchKey[F[_]: ConcurrentEffect](
      watchClient: WatchFs2Grpc[F, Metadata],
      key: String,
      retry: FiniteDuration
  )(implicit timer: Timer[F], eff: Effect[F]): fs2.Stream[F, WatchResponse] = {
    val keyRange = key.asKeyRange
    watchClient
      .watch(
        fs2.Stream.emit(
          WatchRequest(
            WatchRequest.RequestUnion.CreateRequest(
              WatchCreateRequest(
                key = keyRange.start.bytes,
                rangeEnd = keyRange.end.bytes
              )
            )
          )
        ),
        new Metadata()
      ).handleErrorWith {
        case e: StatusRuntimeException if e.getStatus().getCode == Status.Code.UNAVAILABLE =>
          fs2.Stream.eval_(eff.info(s"Retrying watch for $key")) ++
          fs2.Stream.eval_(Timer[F].sleep(retry)) ++
          watchKey(watchClient, key, retry)
        case e =>
          fs2.Stream.eval_(eff.warn("Unrecoverable error", e)) ++ fs2.Stream.raiseError(e)
      }
  }

  private def handleUpdate[F[_]: FlatMap, D](
      update: WatchResponse,
      stateRef: Ref[F, ConfigState[F, D]],
      semaphore: Semaphore[F]
  )(implicit decoder: ConfigParser[D], eff: Effect[F]): F[Unit] =
    if (update.created) {
      eff.info(s"Subscribed on updates with ${update.watchId}")
    } else if (update.canceled) {
      eff.info(s"Etcd watch ${update.watchId} cancelled")
    } else {
      Effect.traverse(for {
        event <- update.events
        kv    <- event.kv
      } yield (event.`type` == Event.EventType.PUT, kv)) {
        case (true, kv) =>
          val key = kv.key.utf8
          decoder.parse(kv.value.utf8) match {
            case Success(parsed) =>
              val newValue = Value(parsed, kv.version)
              val effect = stateRef.get
                .flatTap(_.fireUpdate(key, newValue))
                .map(_.updateValue(key, newValue))
                .flatMap(stateRef.set)
              semaphore.withPermit(effect)
            case Failure(exception) =>
              eff.warn(s"Unable to parse key $key", exception)
          }
        case (false, kv) =>
          semaphore.withPermit(stateRef.update(_.removeKey(kv.value.utf8)))
      }
    }
}
