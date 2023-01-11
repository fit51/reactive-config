package com.github.fit51.reactiveconfig.ce.reloadable

import cats.effect.{ContextShift, IO, Timer}
import cats.effect.Resource
import cats.effect.concurrent.Ref
import cats.instances.int._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.github.fit51.reactiveconfig.reloadable._
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, when}
import org.mockito.invocation.InvocationOnMock
import org.scalatest.{Matchers, WordSpecLike}
import org.scalatestplus.mockito.MockitoSugar

import scala.collection.{immutable, mutable}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.control.NoStackTrace

class ReloadableTest extends WordSpecLike with Matchers with MockitoSugar {

  implicit val shift: ContextShift[IO] = IO.contextShift(global)
  implicit val timer: Timer[IO]        = IO.timer(global)

  trait mocks {
    trait StoppingService[A, B] {
      def stop(a: A): IO[B]
    }

    trait RestartingService[A, B] {
      def restart(a: A, b: B): IO[B]
    }

    case class Data(s: String, i: Int)
  }

  def intervalAtFixedRate(initialDelay: FiniteDuration, period: FiniteDuration, left: Int)(
      f: Int => IO[Unit]
  ): IO[Unit] =
    IO.sleep(initialDelay) >> intervalAtFixedRate(period, 0, left)(f)

  def intervalAtFixedRate(period: FiniteDuration, counter: Int, left: Int)(f: Int => IO[Unit]): IO[Unit] =
    if (left == 0) {
      IO.unit
    } else {
      f(counter) >> IO.sleep(period) >> intervalAtFixedRate(period, counter + 1, left - 1)(f)
    }

  def delayedList[X](list: List[X], period: FiniteDuration)(f: X => IO[Unit]): IO[Unit] =
    list match {
      case immutable.Nil =>
        IO.unit
      case head :: tail =>
        IO.sleep(period) >> f(head) >> delayedList(tail, period)(f)
    }

  val exception = new Exception("oops") with NoStackTrace

  "Reloadable" should {

    "be able to return new data using get" in new mocks {
      Reloadable
        .root[IO](
          initial = "initial"
        ).use { case (reloadable, updater) =>
          for {
            _       <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater.compose(_.toString())).start
            initial <- reloadable.get
            _       <- IO.sleep(3 seconds)
            first   <- reloadable.get
            _       <- IO.sleep(3 seconds)
            second  <- reloadable.get
          } yield {
            initial shouldBe "initial"
            first shouldBe "0"
            second shouldBe "1"
          }
        }.unsafeRunSync()
    }

    "be able to change its reload behaviour using map" in new mocks {
      val function = (s: String) => s"${s}_changed"
      Reloadable
        .root[IO](initial = "initial")
        .flatMap { case (reloadable, updater) =>
          reloadable.map(function).tupleRight(updater)
        }
        .use { case (mappedReloadable, updater) =>
          for {
            _       <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater.compose(_.toString())).start
            initial <- mappedReloadable.get
            _       <- IO.sleep(3 seconds)
            first   <- mappedReloadable.get
            _       <- IO.sleep(3 seconds)
            second  <- mappedReloadable.get
          } yield {
            initial shouldBe "initial_changed"
            first shouldBe "0_changed"
            second shouldBe "1_changed"
          }
        }
        .unsafeRunSync()
    }

    "be able to accept behavior using different ReloadBehaviour" in new mocks {
      val function = (s: String) => IO.pure(s"${s}_changed")

      val stop = mock[StoppingService[String, String]]
      when(stop.stop(any())).thenAnswer((invocation: InvocationOnMock) => IO(invocation.getArgument[String](0)))

      val restart = mock[RestartingService[String, String]]
      when(restart.restart(any(), any())).thenAnswer((invocation: InvocationOnMock) =>
        function(invocation.getArgument[String](0))
      )

      (for {
        (initialReloadable, updater) <- Reloadable.root[IO]("initial")
        mappedReloadable1            <- initialReloadable.mapF(function, Stop((s: String) => stop.stop(s).void))
        mappedReloadable2 <- initialReloadable.mapF(
          function,
          Restart[IO, String, String]((a, b) => restart.restart(a, b), _ => IO.unit)
        )
      } yield (mappedReloadable1, mappedReloadable2, updater)).use {
        case (mappedReloadable1, mappedReloadable2, updater) =>
          for {
            _        <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater.compose(_.toString())).start
            initial1 <- mappedReloadable1.get
            initial2 <- mappedReloadable2.get
            _        <- IO.sleep(3 seconds)
            _ = verify(stop).stop("initial_changed")
            _ = verify(restart).restart("0", "initial_changed")
            first1  <- mappedReloadable1.get
            first2  <- mappedReloadable2.get
            _       <- IO.sleep(3 seconds)
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
          }
      }.unsafeRunSync()
    }

    "be able to change its reload behaviour to dirty computation in pure way using mapF" in new mocks {
      val function = (s: String) => IO(s"${s}_changed")

      Reloadable
        .root[IO]("initial")
        .flatMap { case (reloadable, updater) =>
          reloadable.mapF(function).tupleRight(updater)
        }
        .use { case (mappedReloadable, updater) =>
          for {
            _       <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater.compose(_.toString())).start
            initial <- mappedReloadable.get
            _       <- IO.sleep(3 seconds)
            first   <- mappedReloadable.get
            _       <- IO.sleep(3 seconds)
            second  <- mappedReloadable.get
          } yield {
            initial shouldBe "initial_changed"
            first shouldBe "0_changed"
            second shouldBe "1_changed"
          }
        }
        .timeout(7 seconds).unsafeRunSync()
    }

    "be able to produce new reloadable by combining sources of this and that reloadables " in new mocks {
      val function = (s: String, i: Int) => Data(s, i)

      (for {
        (initialReloadable1, updater1) <- Reloadable.root[IO]("initial")
        (initialReloadable2, updater2) <- Reloadable.root[IO](-1)
        combinedReloadable             <- initialReloadable1.combine(initialReloadable2)(function)
      } yield (combinedReloadable, updater1, updater2)).use { case (combinedReloadable, updater1, updater2) =>
        for {
          _       <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater1.compose(_.toString())).start
          _       <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater2).start
          initial <- combinedReloadable.get
          _       <- IO.sleep(3 seconds)
          first   <- combinedReloadable.get
          _       <- IO.sleep(3 seconds)
          second  <- combinedReloadable.get
        } yield {
          initial shouldBe Data("initial", -1)
          first shouldBe Data("0", 0)
          second shouldBe Data("1", 1)
        }
      }.unsafeRunSync()
    }

    "be able to produce new reloadable by combining sources of this and that reloadables with dirty function " in new mocks {
      val function = (s: String, i: Int) => IO.pure(Data(s, i))

      (for {
        (initialReloadable1, updater1) <- Reloadable.root[IO]("initial")
        (initialReloadable2, updater2) <- Reloadable.root[IO](-1)
        combinedReloadable             <- initialReloadable1.combineF(initialReloadable2)(function)
      } yield (combinedReloadable, updater1, updater2)).use { case (combinedReloadable, updater1, updater2) =>
        for {
          _       <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater1.compose(_.toString())).start
          _       <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater2).start
          initial <- combinedReloadable.get
          _       <- IO.sleep(3 seconds)
          first   <- combinedReloadable.get
          _       <- IO.sleep(3 seconds)
          second  <- combinedReloadable.get
        } yield {
          initial shouldBe Data("initial", -1)
          first shouldBe Data("0", 0)
          second shouldBe Data("1", 1)
        }
      }.timeout(7 seconds).unsafeToFuture()
    }

    "survive after errors in stop handler" in new mocks {
      val stop = mock[StoppingService[String, String]]
      when(stop.stop(any())).thenReturn(
        IO.raiseError(exception),
        IO.raiseError(exception),
        IO.pure("stopped")
      )

      (Reloadable
        .root[IO]("initial")
        .flatMap { case (reloadable, updater) =>
          reloadable.mapF(s => IO.delay(s * 2), Stop((s: String) => stop.stop(s).void)).tupleRight(updater)
        }
        .use { case (mappedReloadable, updater) =>
          for {
            _   <- intervalAtFixedRate(1 second, 1 second, 10)(updater.compose(_.toString())).start
            _   <- IO.sleep(3500 millis)
            cur <- mappedReloadable.get
          } yield {
            verify(stop, times(3)).stop(any())
            cur shouldBe "22"
          }
        } >> IO.sleep(1500 millis)).unsafeRunSync()
      verify(stop, times(4)).stop(any())
    }

    "survive after errors in restart handler" in new mocks {
      val restart      = mock[RestartingService[String, String]]
      var counter: Int = 0
      when(restart.restart(any(), any())).thenAnswer { (invocation: InvocationOnMock) =>
        counter += 1
        if (counter == 2) {
          IO.raiseError(exception)
        } else {
          IO(invocation.getArgument[String](0) + invocation.getArgument[String](1))
        }
      }

      (Reloadable
        .root[IO]("initial")
        .flatMap { case (reloadable, updater) =>
          reloadable
            .mapF(s => IO.delay(s * 2), Restart((a, b) => restart.restart(a, b), (_: String) => IO.unit)).tupleRight(
              updater
            )
        }
        .use { case (mappedReloadable, updater) =>
          for {
            _   <- intervalAtFixedRate(1 second, 1 second, 10)(updater.compose(_.toString())).start
            _   <- IO.sleep(3500 millis)
            cur <- mappedReloadable.get
            _ = verify(restart, times(3)).restart(any(), any())
            _ = verify(restart).restart("0", "initialinitial")
            _ = verify(restart).restart("1", "0initialinitial")
            _ = verify(restart).restart("2", "0initialinitial")
          } yield cur shouldBe "20initialinitial"
        } >> IO.sleep(1500 millis)).unsafeRunSync()

      verify(restart, times(3)).restart(any(), any())
    }

    "don't stop observable even for long stop operations" in new mocks {
      val stop = mock[StoppingService[String, String]]
      when(stop.stop(any())).thenReturn(IO.sleep(3.seconds).as(""))
      (for {
        (initialReloadable, updater) <- Reloadable.root[IO]("initial")
        doubleF <- initialReloadable.mapF(s => IO.delay(s * 2), Stop((s: String) => stop.stop(s).void))
        maskF   <- doubleF.mapF(s => IO.delay(s.take(1) + "***"), Stop((s: String) => stop.stop(s).void))
      } yield (doubleF, maskF, updater)).use { case (doubleF, maskF, updater) =>
        for {
          _       <- intervalAtFixedRate(1 second, 1 second, 10)(updater.compose(_.toString)).start
          _       <- IO.sleep(1100 millis)
          double1 <- doubleF.get
          mask1   <- maskF.get

          _       <- IO.sleep(1 second)
          double2 <- doubleF.get
          mask2   <- maskF.get
        } yield {
          double1 shouldBe "00"
          mask1 shouldBe "0***"
          double2 shouldBe "11"
          mask2 shouldBe "1***"
        }
      }.unsafeRunSync()
    }

    "do side effects using forEachF" in new mocks {
      val buffer = mutable.ListBuffer[String]()
      Reloadable
        .root[IO]("initial")
        .flatMap { case (reloadable, updater) =>
          reloadable.mapF(s => IO.delay(s * 2)).tupleRight(updater)
        }
        .use { case (mappedReloadable, updater) =>
          intervalAtFixedRate(1 second, 1 second, 10)(updater.compose(_.toString())).start >>
            mappedReloadable.forEachF(s => IO.delay(buffer += s))
        }
        .timeoutTo(2500 millis, IO.unit)
        .unsafeRunSync()

      buffer.toList shouldBe List("initialinitial", "00", "11")
    }

    "filter duplicated keys" in new mocks {
      val buffer1 = mutable.ListBuffer[Int]()
      val buffer2 = mutable.ListBuffer[Int]()
      (for {
        (initialReloadable, updater) <- Reloadable.root[IO](0)
        filteredR                    <- initialReloadable.distinctByKey(i => i * i * i)
        absR                         <- filteredR.map(_.abs)
        filtered2R                   <- absR.distinctByKey(identity)
      } yield (filteredR, filtered2R, updater)).use { case (filteredR, filtered2R, updater) =>
        for {
          _      <- delayedList(List(1, 1, -1, -1, 2, 2, 2, 1, 1, -1, -1, 3, -3), 1 second)(updater).start
          fiber1 <- filteredR.forEachF(i => IO.delay(buffer1 += i)).start
          fiber2 <- filtered2R.forEachF(i => IO.delay(buffer2 += i)).start
          _      <- fiber1.join
          _      <- fiber2.join
        } yield ()
      }.timeoutTo(15 seconds, IO.unit).unsafeRunSync()

      buffer1.toList shouldBe List(0, 1, -1, 2, 1, -1, 3, -3)
      buffer2.toList shouldBe List(0, 1, 2, 1, 3)
    }

    "handle slow and fast mapped reloadables" in new mocks {
      (for {
        (initialReloadable, updater) <- Reloadable.root[IO](0)
        fast1Reloadable              <- initialReloadable.map(_ + 1)
        fast2Reloadable              <- initialReloadable.map(_ * 2)
        slowReloadable               <- fast2Reloadable.mapF(i => IO.sleep(1 second).as(i + 1))
      } yield (fast1Reloadable, fast2Reloadable, slowReloadable, updater)).use {
        case (fast1Reloadable, fast2Reloadable, slowReloadable, updater) =>
          for {
            _               <- delayedList(List(1, 2, 3, 4), 0 second)(updater).start
            _               <- IO.sleep(2500 millis)
            plusOne1        <- fast1Reloadable.get
            doubled1        <- fast2Reloadable.get
            doubledPlusOne1 <- slowReloadable.get

            _               <- IO.sleep(1 second)
            plusOne2        <- fast1Reloadable.get
            doubled2        <- fast2Reloadable.get
            doubledPlusOne2 <- slowReloadable.get
          } yield {
            plusOne1 shouldBe 4
            doubled1 shouldBe 6
            doubledPlusOne1 shouldBe 5

            plusOne2 shouldBe 5
            doubled2 shouldBe 8
            doubledPlusOne2 shouldBe 7
          }
      }.unsafeRunSync()
    }

    "handle slow and fast combined reloadables" in new mocks {
      (for {
        (initialReloadable1, updater1) <- Reloadable.root[IO](0)
        (initialReloadable2, updater2) <- Reloadable.root[IO](0)

        fastMappedReloadable <- initialReloadable1.map(_ + 1)
        slowMappedReloadable <- initialReloadable2.mapF(i => IO.sleep(1 second).as(i - 1))

        combined <- fastMappedReloadable.combineF(slowMappedReloadable)((a, b) => IO.sleep(2 seconds).as((a, b)))
      } yield (combined, updater1, updater2)).use { case (combined, updater1, updater2) =>
        for {
          _ <- delayedList(List(1, 2, 3, 4), 0 second)(updater1).start
          _ <- delayedList(List(-1, -2, -3, -4), 0 second)(updater2).start

          _      <- IO.sleep(2500 millis)
          value1 <- combined.get

          _      <- IO.sleep(2 seconds)
          value2 <- combined.get

          _      <- IO.sleep(2 seconds)
          value3 <- combined.get
        } yield {
          value1 shouldBe (2, -1)
          value2 shouldBe (2, -2)
          value3 shouldBe (3, -2)
        }
      }.unsafeRunSync()
    }

    "map with resource" in new mocks {
      Ref
        .of[IO, List[String]](Nil).flatTap { closedRef =>
          (for {
            (reloadable, updater) <- Reloadable.root[IO]("initial")
            mappedReloadable <- reloadable.mapResource { str =>
              Resource.make(IO.delay(str.toUpperCase))(value => closedRef.update(value :: _))
            }
          } yield (mappedReloadable, updater)).use { case (reloadable, updater) =>
            for {
              _            <- delayedList(List(0, 1, 2), 1 second)(updater.compose(_.toString())).start
              initial      <- reloadable.get
              _            <- IO.sleep(1500 millis)
              second       <- reloadable.get
              secondClosed <- closedRef.get
              _            <- IO.sleep(1 second)
              third        <- reloadable.get
              thirdClosed  <- closedRef.get
            } yield {
              initial shouldBe "INITIAL"
              second shouldBe "0"
              third shouldBe "1"
              secondClosed shouldBe List("INITIAL")
              thirdClosed shouldBe List("0", "INITIAL")
            }
          }
        }.flatMap(_.get).map { closed =>
          closed shouldBe List("1", "0", "INITIAL")
        }
    }
  }
}
