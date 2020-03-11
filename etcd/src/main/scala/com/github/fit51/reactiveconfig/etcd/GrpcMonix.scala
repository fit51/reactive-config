package com.github.fit51.reactiveconfig.etcd

import io.grpc.stub.StreamObserver
import monix.execution.Ack.{Continue, Stop}
import monix.reactive.OverflowStrategy
import monix.reactive.observers.{BufferedSubscriber, Subscriber}

object GrpcMonix {
  class StopException extends Exception

  def monixToGrpcObserverBuffered[T](subscriber: Subscriber[T]): StreamObserver[T] = {
    //Using system default batch size (1024) of BatchedExecution model
    val buffer = BufferedSubscriber.synchronous[T](subscriber, OverflowStrategy.Unbounded)
    new StreamObserver[T] {
      override def onNext(value: T): Unit = buffer.onNext(value) match {
        case Continue => ()
        case Stop     => throw new StopException
      }
      override def onError(t: Throwable): Unit = buffer.onError(t)
      override def onCompleted(): Unit         = buffer.onComplete()
    }
  }
}
