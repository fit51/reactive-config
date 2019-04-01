package com.github.fit51.reactiveconfig.reloadable

import cats.effect.IO
import monix.reactive.Observable
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{verify, when}
import org.scalatest.{Matchers, WordSpecLike}
import org.scalatest.mockito.MockitoSugar
import scala.concurrent.duration._
import monix.execution.Scheduler.Implicits.global
import org.mockito.invocation.InvocationOnMock
import scala.concurrent.Await

class ReloadableImplTest extends WordSpecLike with Matchers with MockitoSugar {

  trait mocks {
    trait StoppingService[A, B] {
      def stop(a: A): IO[B]
    }

    trait RestartingService[A, B] {
      def restart(a: A, b: B): IO[B]
    }

    case class Data(s: String, i: Int)
  }

  "Reloadable" should {

    "be able to return new data using get" in new mocks {
      val reloadable = ReloadableImpl[IO, String, String](
        initial = "initial",
        start = IO.pure,
        ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toString)
      )

      reloadable.get.shouldEqual("initial")
      Thread.sleep(3000)
      reloadable.get.shouldEqual("0")
      Thread.sleep(3000)
      reloadable.get.shouldEqual("1")
    }

    "be able to change its reload behaviour using map" in new mocks {
      val initialReloadable = ReloadableImpl[IO, String, String](
        initial = "initial",
        start = IO.pure,
        ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toString)
      )

      val function = (s: String) => s"${s}_changed"

      val mappedReloadable = initialReloadable.map(function, Simple[IO, String, String])

      mappedReloadable.get.shouldEqual("initial_changed")
      Thread.sleep(3000)
      mappedReloadable.get.shouldEqual("0_changed")
      Thread.sleep(3000)
      mappedReloadable.get.shouldEqual("1_changed")
    }

    "be able to accept behavior using different ReloadBehaviour" in new mocks {
      val initialReloadable = ReloadableImpl[IO, String, String](
        initial = "initial",
        start = IO.pure,
        ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toString)
      )

      val function = (s: String) => s"${s}_changed"

      //FOR STOP BEHAVIOUR

      val stop = mock[StoppingService[String, String]]
      when(stop.stop(any())).thenAnswer((invocation: InvocationOnMock) => IO(invocation.getArgument[String](0)))

      val mappedReloadable1 = initialReloadable.map(function, Stop[IO, String, String]((s: String) => stop.stop(s)))

      //FOR RESTART BEHAVIOUR

      val restart = mock[RestartingService[String, String]]
      when(restart.restart(any(), any())).thenAnswer((invocation: InvocationOnMock) =>
        IO(function(invocation.getArgument[String](0))))

      val mappedReloadable2 =
        initialReloadable.map(function, Restart[IO, String, String]((a: String, b: String) => restart.restart(a, b)))

      //Assertions

      mappedReloadable1.get.shouldEqual("initial_changed")
      mappedReloadable2.get.shouldEqual("initial_changed")
      Thread.sleep(3000)
      verify(stop).stop("initial_changed")
      verify(restart).restart("0", "initial_changed")
      mappedReloadable1.get.shouldEqual("0_changed")
      mappedReloadable2.get.shouldEqual("0_changed")
      Thread.sleep(3000)
      mappedReloadable1.get.shouldEqual("1_changed")
      mappedReloadable2.get.shouldEqual("1_changed")
      verify(stop).stop("0_changed")
      verify(restart).restart("1", "0_changed")
    }

    "be able to change its reload behaviour to dirty computation in pure way using mapF" in new mocks {
      val initialReloadable = ReloadableImpl[IO, String, String](
        initial = "initial",
        start = IO.pure,
        ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toString)
      )

      val function = (s: String) => IO(s"${s}_changed")

      val mappedReloadable = initialReloadable.mapF(function, Simple[IO, String, String])

      Await.result(
        mappedReloadable.map { reloadable =>
          reloadable.get.shouldEqual("initial_changed")
          Thread.sleep(3000)
          reloadable.get.shouldEqual("0_changed")
          Thread.sleep(3000)
          reloadable.get.shouldEqual("1_changed")
        }.unsafeToFuture(),
        7 seconds
      )
    }

    "be able to produce new reloadable by combining sources of this and that reloadables " in new mocks {
      val initialReloadable1 = ReloadableImpl[IO, String, String](
        initial = "initial",
        start = IO.pure,
        ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toString)
      )

      val initialReloadable2 = ReloadableImpl[IO, Int, Int](
        initial = -1,
        start = IO.pure,
        ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toInt)
      )

      val function = (s: String, i: Int) => Data(s, i)

      val combinedReloadable = initialReloadable1.combine(initialReloadable2)(function, Simple[IO, (String, Int), Data])

      combinedReloadable.get.shouldEqual(Data("initial", -1))
      Thread.sleep(3000)
      combinedReloadable.get.shouldEqual(Data("0", 0))
      Thread.sleep(3000)
      combinedReloadable.get.shouldEqual(Data("1", 1))
    }

    "be able to produce new reloadable by combining sources of this and that reloadables with dirty function " in new mocks {
      val initialReloadable1 = ReloadableImpl[IO, String, String](
        initial = "initial",
        start = IO.pure,
        ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toString)
      )

      val initialReloadable2 = ReloadableImpl[IO, Int, Int](
        initial = -1,
        start = IO.pure,
        ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toInt)
      )

      val function = (s: String, i: Int) => IO(Data(s, i))

      val combinedReloadable =
        initialReloadable1.combineF(initialReloadable2)(function, Simple[IO, (String, Int), Data])

      Await.result(
        combinedReloadable.map { reloadable =>
          reloadable.get.shouldEqual(Data("initial", -1))
          Thread.sleep(3000)
          reloadable.get.shouldEqual(Data("0", 0))
          Thread.sleep(3000)
          reloadable.get.shouldEqual(Data("1", 1))
        }.unsafeToFuture(),
        7 seconds
      )
    }
  }
}
