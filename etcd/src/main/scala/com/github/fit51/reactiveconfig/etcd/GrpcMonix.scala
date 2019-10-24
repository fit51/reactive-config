package com.github.fit51.reactiveconfig.etcd

import io.grpc.stub.StreamObserver
import monix.reactive.observers.Subscriber

object GrpcMonix {
  def monixSubscriberToGrpcObserver[T](subscriber: Subscriber[T]): StreamObserver[T] =
    new StreamObserver[T] {
      override def onError(t: Throwable): Unit = subscriber.onError(t)
      override def onCompleted(): Unit         = subscriber.onComplete()
      // No backpressure here, breaking the observer contract
      override def onNext(value: T): Unit = subscriber.onNext(value)
    }
}
