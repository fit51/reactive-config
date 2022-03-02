package com.github.fit51.reactiveconfig.zio.etcd

import cats.data.NonEmptySet
import com.github.fit51.reactiveconfig.Value
import com.github.fit51.reactiveconfig.config.ConfigState
import com.github.fit51.reactiveconfig.etcd._
import com.github.fit51.reactiveconfig.etcd.config.Utils
import com.github.fit51.reactiveconfig.etcd.gen.kv.Event.EventType
import com.github.fit51.reactiveconfig.etcd.gen.rpc._
import com.github.fit51.reactiveconfig.etcd.gen.rpc.ZioRpc._
import com.github.fit51.reactiveconfig.parser.ConfigParser
import com.github.fit51.reactiveconfig.typeclasses.Effect
import com.github.fit51.reactiveconfig.zio.config.AbstractReactiveConfig
import com.github.fit51.reactiveconfig.zio.reloadable.Reloadable._
import io.grpc.Status
import zio._
import zio.clock.Clock
import zio.duration.Duration
import zio.stream.ZStream

import scala.util.{Failure, Success}

class EtcdReactiveConfig[D](stateRef: RefM[ConfigState[UIO, D]]) extends AbstractReactiveConfig[D](stateRef)

object EtcdReactiveConfig {

  private val uioEffect: Effect[UIO] = Effect[UIO]

  def live[D: ConfigParser: Tag](
      prefixes: NonEmptySet[String],
      retryDuration: Duration
  ) =
    (for {
      _           <- IO.fail(new IntersectionError(prefixes)).when(Utils.doIntersect(prefixes))
      kvClient    <- ZIO.service[KVClient.ZService[Any, Any]]
      watchClient <- ZIO.service[WatchClient.ZService[Any, Any]]
      stateRef    <- loadInitials(prefixes, kvClient).map(ConfigState[UIO, D](_, Map.empty)) >>= ZRefM.make
      _ <- prefixes.foldLeft[ZStream[Clock, GrpcError, WatchResponse]](ZStream.empty) { case (acc, prefix) =>
        acc.merge(watchKey(watchClient, prefix, retryDuration))
      } foreach (handleUpdate(_, stateRef)) fork
    } yield new EtcdReactiveConfig(stateRef)).toLayer

  private def loadInitials[D](
      prefixes: NonEmptySet[String],
      kvClient: KVClient.ZService[Any, Any]
  )(implicit decoder: ConfigParser[D]): IO[GrpcError, Map[String, Value[D]]] =
    ZIO.foreachPar(prefixes.toNonEmptyList.toList.toVector) { key =>
      val keyRange = key.asKeyRange
      kvClient
        .range(
          RangeRequest(
            key = keyRange.start.bytes,
            rangeEnd = keyRange.end.bytes
          )
        ).mapError(new GrpcError(_)).map(_.kvs.toVector)
    } map (_.flatten) map { allKvs =>
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
      ZIO.foreach(errors) { case (key, message) =>
        uioEffect.warn(s"Unable to parse key $key: $message")
      } as parsedKvs.toMap
    }

  private def watchKey(
      watchClient: WatchClient.ZService[Any, Any],
      key: String,
      retry: Duration
  ): ZStream[Clock, GrpcError, WatchResponse] = {
    val keyRange = key.asKeyRange
    watchClient.watch(
      ZStream(
        WatchRequest(
          WatchRequest.RequestUnion.CreateRequest(
            WatchCreateRequest(
              key = keyRange.start.bytes,
              rangeEnd = keyRange.end.bytes
            )
          )
        )
      )
    ) catchAll { status =>
      if (status.getCode() == Status.Code.UNAVAILABLE) {
        ZStream.execute(uioEffect.info(s"Retrying watch for $key")) ++
        ZStream.execute(URIO.sleep(retry)) ++
        watchKey(watchClient, key, retry)
      } else {
        ZStream.execute(uioEffect.warn(s"Unrecoverable error: $status")) ++ ZStream.fail(new GrpcError(status))
      }
    }
  }

  private def handleUpdate[D](
      update: WatchResponse,
      stateRef: RefM[ConfigState[UIO, D]]
  )(implicit decoder: ConfigParser[D]): UIO[Unit] =
    if (update.created) {
      uioEffect.info(s"Subscribed on updates with ${update.watchId}")
    } else if (update.canceled) {
      uioEffect.info(s"Etcd watch ${update.watchId} cancelled")
    } else {
      ZIO.foreach_(for {
        event <- update.events
        kv    <- event.kv
      } yield (event.`type` == EventType.PUT, kv)) {
        case (true, kv) =>
          val key = kv.key.utf8
          decoder.parse(kv.value.utf8) match {
            case Success(parsed) =>
              stateRef.update { state =>
                val newValue = Value(parsed, kv.version)
                state.fireUpdate(key, newValue).as(state.updateValue(key, newValue))
              }
            case Failure(exception) =>
              uioEffect.warn(s"Unable to parse key $key: ${exception.getMessage()}")
          }
        case (false, kv) =>
          stateRef.update(state => UIO.succeed(state.removeKey(kv.value.utf8)))
      }
    }
}

sealed abstract class ReactiveEtcdConfigException(message: String) extends Exception(message)

final class IntersectionError(prefixes: NonEmptySet[String])
    extends ReactiveEtcdConfigException("Prefixes should not intersect")

final class GrpcError(status: Status) extends ReactiveEtcdConfigException(s"GRPC exception $status")
