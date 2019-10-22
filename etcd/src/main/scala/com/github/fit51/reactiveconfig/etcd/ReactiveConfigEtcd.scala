package com.github.fit51.reactiveconfig.etcd

import cats.effect.{Async, ContextShift}
import com.github.fit51.reactiveconfig.config.{ReactiveConfig, ReactiveConfigImpl}
import com.github.fit51.reactiveconfig.parser.ConfigParser
import monix.execution.Scheduler
import cats.syntax.flatMap._
import cats.syntax.functor._
import monix.eval.TaskLike

object ReactiveConfigEtcd {

  def apply[F[_]: Async: ContextShift: TaskLike, ParsedData](
      channelManager: ChannelManager,
      prefix: String = ""
  )(implicit scheduler: Scheduler, configParser: ConfigParser[ParsedData]): F[ReactiveConfig[F, ParsedData]] = {
    val F = implicitly[Async[F]]
    for {
      etcdClient <- F.pure(new EtcdClient[F](channelManager) with Watch[F])
      storage    <- F.pure(new EtcdConfigStorage[F, ParsedData](etcdClient, prefix))
      config     <- ReactiveConfigImpl(storage)
    } yield config
  }

}
