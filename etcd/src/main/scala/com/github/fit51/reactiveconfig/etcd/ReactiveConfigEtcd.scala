package com.github.fit51.reactiveconfig.etcd

import cats.data.NonEmptySet
import cats.effect.{Resource, Sync}
import cats.instances.string._
import cats.syntax.applicative._
import com.github.fit51.reactiveconfig.config.ReactiveConfig
import com.github.fit51.reactiveconfig.parser.ConfigParser
import monix.eval.TaskLift
import monix.eval.TaskLike
import monix.execution.Scheduler

object ReactiveConfigEtcd {

  final class IntersectionError(prefixes: NonEmptySet[String]) extends Exception("Prefixes should not intersect.")

  def apply[F[_]: Sync: TaskLike: TaskLift, ParsedData](
      etcdClient: EtcdClient[F] with Watch[F],
      prefixes: NonEmptySet[String] = NonEmptySet.one("")
  )(implicit scheduler: Scheduler, configParser: ConfigParser[ParsedData]): Resource[F, ReactiveConfig[F, ParsedData]] =
    for {
      _       <- Resource.liftF(Sync[F].raiseError(new IntersectionError(prefixes)).whenA(doIntersect(prefixes)))
      storage <- Resource.pure(new EtcdConfigStorage[F, ParsedData](etcdClient, prefixes))
      config  <- ReactiveConfig(storage)
    } yield config

  def doIntersect(prefixes: NonEmptySet[String]): Boolean =
    !prefixes.forall { prefix1 =>
      prefixes.forall { prefix2 =>
        prefix1 == prefix2 || !prefix1.startsWith(prefix2)
      }
    }
}
