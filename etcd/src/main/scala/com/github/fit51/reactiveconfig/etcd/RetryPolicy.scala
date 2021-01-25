package com.github.fit51.reactiveconfig.etcd

import scala.concurrent.duration.FiniteDuration

trait RetryPolicy {
  def next: FiniteDuration
}

case class SimpleDelayPolicy(delay: FiniteDuration) extends RetryPolicy {
  override def next: FiniteDuration = delay
}
