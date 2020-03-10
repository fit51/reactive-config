package com.github.fit51.reactiveconfig.etcd

import io.grpc.stub.StreamObserver
import monix.reactive.observers.Subscriber

object GrpcMonix {
  def monixToGrpcObserver[T](subscriber: Subscriber[T]): StreamObserver[T] = {
    //Using system default batch size (1024) of BatchedExecution model
    val rSubscriber = Subscriber.toReactiveSubscriber(subscriber)
    new StreamObserver[T] {
      override def onNext(value: T): Unit      = rSubscriber.onNext(value)
      override def onError(t: Throwable): Unit = rSubscriber.onError(t)
      override def onCompleted(): Unit         = rSubscriber.onComplete()
    }
  }
}
