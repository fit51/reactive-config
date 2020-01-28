package com.github.fit51.reactiveconfig.reloadable

import cats.effect.Clock
import cats.effect.IO
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable
import org.mockito.ArgumentMatchers.any
import org.mockito.invocation.InvocationOnMock
import org.mockito.Mockito.{never, times, verify, when}
import org.scalatest.{Matchers, WordSpecLike}
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.collection.mutable
import scala.concurrent.ExecutionContext

class ReloadableImplTest extends WordSpecLike with Matchers with MockitoSugar {

  trait mocks {
    trait StoppingService[A, B] {
      def stop(a: A): IO[B]
    }

    trait RestartingService[A, B] {
      def restart(a: A, b: B): IO[B]
    }

    case class Data(s: String, i: Int)

    implicit val timer = new cats.effect.Timer[IO] {
      override def clock: Clock[IO] = Clock.create
      override def sleep(duration: FiniteDuration): IO[Unit] =
        Task.sleep(duration).to[IO]
    }

    implicit val shift = new cats.effect.ContextShift[IO] {
      override def shift: IO[Unit] = Task.shift.to[IO]
      override def evalOn[A](ec: ExecutionContext)(fa: IO[A]): IO[A] = fa
    }
  }

  "Reloadable" should {

    "be able to return new data using get" in new mocks {
      (for {
        reloadable <- Reloadable[IO, String](
          initial = "initial",
          ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toString)
        )
        initial <- reloadable.get
        _ <- IO.sleep(3 seconds)
        first <- reloadable.get
        _ <- IO.sleep(3 seconds)
        second <- reloadable.get
      } yield {
        initial shouldBe "initial"
        first shouldBe "0"
        second shouldBe "1"
      }).unsafeRunSync()
    }

    "be able to change its reload behaviour using map" in new mocks {
      val function = (s: String) => s"${s}_changed"
      (for {
        initialReloadable <- Reloadable[IO, String](
          initial = "initial",
          ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toString)
        )
        mappedReloadable <- initialReloadable.map(function)
        initial <- mappedReloadable.get
        _ <- IO.sleep(3 seconds)
        first <- mappedReloadable.get
        _ <- IO.sleep(3 seconds)
        second <- mappedReloadable.get
      } yield {
        initial shouldBe "initial_changed"
        first shouldBe "0_changed"
        second shouldBe "1_changed"
      }).unsafeRunSync()
    }

    "be able to accept behavior using different ReloadBehaviour" in new mocks {
      val function = (s: String) => IO.pure(s"${s}_changed")

      val stop = mock[StoppingService[String, String]]
      when(stop.stop(any())).thenAnswer((invocation: InvocationOnMock) => IO(invocation.getArgument[String](0)))

      val restart = mock[RestartingService[String, String]]
      when(restart.restart(any(), any()))
        .thenAnswer((invocation: InvocationOnMock) => function(invocation.getArgument[String](0)))

      (for {
        initialReloadable <- Reloadable[IO, String](
          initial = "initial",
          ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toString)
        )
        mappedReloadable1 <- initialReloadable.mapF(function, Stop((s: String) => stop.stop(s)))
        mappedReloadable2 <- initialReloadable.mapF(function, Restart((a: String, b: String) => restart.restart(a, b)))

        initial1 <- mappedReloadable1.get
        initial2 <- mappedReloadable2.get
        _ <- IO.sleep(3 seconds)
        _ = verify(stop).stop("initial_changed")
        _ = verify(restart).restart("0", "initial_changed")
        first1 <- mappedReloadable1.get
        first2 <- mappedReloadable2.get
        _ <- IO.sleep(3 seconds)
        second1 <- mappedReloadable1.get
        second2 <- mappedReloadable2.get
        _ = verify(stop).stop("0_changed")
        _ = verify(restart).restart("1", "0_changed")
      } yield {
        initial1 shouldBe "initial_changed"
        initial2 shouldBe "initial_changed"
        first1 shouldBe "0_changed"
        first2 shouldBe "0_changed"
        second1 shouldBe "1_changed"
        second2 shouldBe "1_changed"
      }).unsafeRunSync()
    }

    "be able to change its reload behaviour to dirty computation in pure way using mapF" in new mocks {
      val function = (s: String) => IO(s"${s}_changed")

      val future = (for {
        initialReloadable <- Reloadable[IO, String](
          initial = "initial",
          ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toString)
        )
        mappedReloadable <- initialReloadable.mapF(function)

        initial <- mappedReloadable.get
        _ <- IO.sleep(3 seconds)
        first <- mappedReloadable.get
        _ <- IO.sleep(3 seconds)
        second <- mappedReloadable.get
      } yield {
        initial shouldBe "initial_changed"
        first shouldBe "0_changed"
        second shouldBe "1_changed"
      }).unsafeToFuture

      Await.result(future, 7 seconds)
    }

    "be able to produce new reloadable by combining sources of this and that reloadables " in new mocks {
      val function = (s: String, i: Int) => Data(s, i)

      (for {
        initialReloadable1 <- Reloadable[IO, String](
          initial = "initial",
          ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toString)
        )
        initialReloadable2 <- Reloadable[IO, Int](
          initial = -1,
          ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toInt)
        )

        combinedReloadable <- initialReloadable1.combine(initialReloadable2)(function)
        initial <- combinedReloadable.get
        _ <- IO.sleep(3 seconds)
        first <- combinedReloadable.get
        _ <- IO.sleep(3 seconds)
        second <- combinedReloadable.get
      } yield {
        initial shouldBe Data("initial", -1)
        first shouldBe Data("0", 0)
        second shouldBe Data("1", 1)
      }).unsafeRunSync()
    }

    "be able to produce new reloadable by combining sources of this and that reloadables with dirty function " in new mocks {
      val function = (s: String, i: Int) => IO.pure(Data(s, i))

      val future = (for {
        initialReloadable1 <- Reloadable[IO, String](
          initial = "initial",
          ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toString)
        )
        initialReloadable2 <- Reloadable[IO, Int](
          initial = -1,
          ob = Observable.intervalAtFixedRate(2 second, 2 second).take(2).map(_.toInt)
        )

        combinedReloadable <- initialReloadable1.combineF(initialReloadable2)(function)
        initial <- combinedReloadable.get
        _ <- IO.sleep(3 seconds)
        first <- combinedReloadable.get
        _ <- IO.sleep(3 seconds)
        second <- combinedReloadable.get
      } yield {
        initial shouldBe Data("initial", -1)
        first shouldBe Data("0", 0)
        second shouldBe Data("1", 1)
      }).unsafeToFuture()

      Await.result(future, 7 seconds)
    }

    "survive after errors in stop handler" in new mocks {
      val stop = mock[StoppingService[String, String]]
      when(stop.stop(any())).thenReturn(
        IO.raiseError(new Exception("oops")),
        IO.raiseError(new Exception("oops")),
        IO.pure("stopped")
      )

      (for {
        initialReloadable <- Reloadable[IO, String](
          initial = "initial",
          ob = Observable.intervalAtFixedRate(1 second, 1 second).map(_.toString)
        )
        mappedReloadable <- initialReloadable.mapF(s => IO.delay(s * 2), Stop((s: String) => stop.stop(s)))

        _ <- IO.sleep(3500 millis)
        cur <- mappedReloadable.get
        _ = verify(stop, times(3)).stop(any())

        _ <- mappedReloadable.stop
        _ <- IO.sleep(1500 millis)
        _ = verify(stop, times(3)).stop(any())
      } yield {
        cur shouldBe "22"
      }).unsafeRunSync()
    }

    "survive after errors in restart handler" in new mocks {
      val restart = mock[RestartingService[String, String]]
      var counter: Int = 0
      when(restart.restart(any(), any())).thenAnswer((invocation: InvocationOnMock) => {
        counter += 1
        if (counter == 2) {
          IO.raiseError(new Exception("!"))
        } else {
          IO(invocation.getArgument[String](0) + invocation.getArgument[String](1))
        }
      })

      (for {
        initialReloadable <- Reloadable[IO, String](
          initial = "initial",
          ob = Observable.intervalAtFixedRate(1 second, 1 second).map(_.toString)
        )

        mappedReloadable <- initialReloadable.mapF(s => IO.delay(s * 2), Restart((a, b) => restart.restart(a, b)))

        _ <- IO.sleep(3500 millis)
        cur <- mappedReloadable.get
        _ = verify(restart, times(3)).restart(any(), any())
        _ = verify(restart).restart("0", "initialinitial")
        _ = verify(restart).restart("1", "0initialinitial")
        _ = verify(restart).restart("2", "0initialinitial")

        _ <- mappedReloadable.stop
        _ <- IO.sleep(1500 millis)
        _ = verify(restart, times(3)).restart(any(), any())
      } yield {
        cur shouldBe "20initialinitial"
      }).unsafeRunSync()
    }

    "don't stop observable even for long stop operations" in new mocks {
      val stop = mock[StoppingService[String, String]]
      when(stop.stop(any())).thenReturn(IO.never)
      (for {
        initialReloadable <- Reloadable[IO, String](
          initial = "initial",
          ob = Observable.intervalAtFixedRate(1 second, 1 second).map(_.toString)
        )
        doubleF <- initialReloadable.mapF(s => IO.delay(s * 2), Stop((s: String) => stop.stop(s)))
        maskF <- doubleF.mapF(s => IO.delay(s.take(1) + "***"), Stop((s: String) => stop.stop(s)))

        _ <- IO.sleep(1100 millis)
        double1 <- doubleF.get
        mask1 <- maskF.get

        _ <- IO.sleep(1 second)
        double2 <- doubleF.get
        mask2 <- maskF.get

        _ <- initialReloadable.stop
      } yield {
        double1 shouldBe "00"
        mask1 shouldBe "0***"
        double2 shouldBe "11"
        mask2 shouldBe "1***"
      }).unsafeRunSync()
    }

    "do side effects using forEachF" in new mocks {
      val buffer = mutable.ListBuffer[String]()
      (for {
        initialReloadable <- Reloadable[IO, String](
          initial = "initial",
          ob = Observable.intervalAtFixedRate(1 second, 1 second).map(_.toString)
        )
        doubleF <- initialReloadable.mapF(s => IO.delay(s * 2))

        fiber <- doubleF
          .forEachF(s => IO.delay(buffer += s))
          .start

        _ <- IO.sleep(2500 millis)
        _ <- fiber.cancel
      } yield {
        buffer.toList shouldBe List("initialinitial", "00", "11")
      }).unsafeRunSync()
    }
  }
}
