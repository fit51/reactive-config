package com.github.fit51.reactiveconfig.etcd

import cats.data.NonEmptySet
import cats.effect.{Async, ContextShift}
import cats.implicits._
import com.github.fit51.reactiveconfig.config.{ReactiveConfig, ReactiveConfigImpl}
import com.github.fit51.reactiveconfig.parser.ConfigParser
import monix.execution.Scheduler
import monix.eval.TaskLike

object ReactiveConfigEtcd {

  final class IntersectionError(prefixes: List[String]) extends Exception("Prefixes should not intersect.")

  def apply[F[_]: Async: ContextShift: TaskLike, ParsedData](
      etcdClient: EtcdClient[F] with Watch[F],
      prefixes: NonEmptySet[String] = NonEmptySet.one("")
  )(implicit scheduler: Scheduler, configParser: ConfigParser[ParsedData]): F[ReactiveConfig[F, ParsedData]] = {
    val F = implicitly[Async[F]]
    for {
      _       <- F.raiseError(new IntersectionError(prefixes.toList)).whenA(doIntersect(prefixes))
      storage <- F.pure(EtcdConfigStorage[F, ParsedData](etcdClient, prefixes))
      config  <- ReactiveConfigImpl(storage)
    } yield config
  }

  def doIntersect(prefixes: NonEmptySet[String]): Boolean = {
    !prefixes.forall { prefix1 =>
      prefixes.forall { prefix2 =>
        prefix1 == prefix2 || !prefix1.startsWith(prefix2)
      }
    }
  }
}
