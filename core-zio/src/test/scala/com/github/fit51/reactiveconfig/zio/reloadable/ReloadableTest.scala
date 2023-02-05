package com.github.fit51.reactiveconfig.zio.reloadable

import com.github.fit51.reactiveconfig.reloadable._
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, when}
import org.mockito.invocation.InvocationOnMock
import org.scalatest.{Matchers, WordSpecLike}
import org.scalatestplus.mockito.MockitoSugar
import zio.{Reloadable => _, _}

import scala.collection.{immutable, mutable}
import scala.util.control.NoStackTrace

class ReloadableTest extends WordSpecLike with Matchers with MockitoSugar {

  val runtime = Runtime.default.withEnvironment {
    ZEnvironment(Clock.ClockLive)
  }

  trait UStoppingService[A, B] {
    def stop(a: A): UIO[B]
  }

  trait StoppingService[R, A, B] {
    def stop(a: A): ZIO[R, Throwable, A]
  }

  trait URestartingService[A, B] {
    def restart(a: A, b: B): UIO[B]
  }

  trait RestartingService[A, B] {
    def restart(a: A, b: B): Task[B]
  }

  case class Data(s: String, i: Int)

  trait mocks {}

  def intervalAtFixedRate(initialDelay: Duration, period: Duration, left: Int)(
      f: Int => UIO[Unit]
  ): RIO[Clock, Unit] =
    ZIO.sleep(initialDelay) *> intervalAtFixedRate(period, 0, left)(f)

  def intervalAtFixedRate(period: Duration, counter: Int, left: Int)(f: Int => UIO[Unit]): RIO[Clock, Unit] =
    if (left == 0) {
      ZIO.unit
    } else {
      f(counter) *> ZIO.sleep(period) *> intervalAtFixedRate(period, counter + 1, left - 1)(f)
    }

  def delayedList[X](list: List[X], period: Duration)(f: X => UIO[Unit]): RIO[Clock, Unit] =
    list match {
      case immutable.Nil =>
        ZIO.unit
      case head :: tail =>
        ZIO.sleep(period) *> f(head) *> delayedList(tail, period)(f)
    }

  val exception = new Exception("oops") with NoStackTrace

  "ZioReloadable" should {

    "be able to return new data using get" in new mocks {
      val zio = ZIO.scoped {
        for {
          (reloadable, updater) <- Reloadable.root("initial")
          _                     <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater.compose(_.toString())).fork
          initial               <- reloadable.get
          _                     <- ZIO.sleep(3 seconds)
          first                 <- reloadable.get
          _                     <- ZIO.sleep(3 seconds)
          second                <- reloadable.get
        } yield {
          initial shouldBe "initial"
          first shouldBe "0"
          second shouldBe "1"
        }
      }
      Unsafe.unsafe(implicit u => runtime.unsafe.run(zio).getOrThrow())
    }

    "be able to change its reload behaviour using map" in new mocks {
      val function = (s: String) => s"${s}_changed"
      val zio =
        for {
          (reloadable, updater) <- Reloadable.root("initial")
          mappedReloadable      <- reloadable.map(function)
          _                     <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater.compose(_.toString())).fork
          initial               <- mappedReloadable.get
          _                     <- ZIO.sleep(3 seconds)
          first                 <- mappedReloadable.get
          _                     <- ZIO.sleep(3 seconds)
          second                <- mappedReloadable.get
        } yield {
          initial shouldBe "initial_changed"
          first shouldBe "0_changed"
          second shouldBe "1_changed"
        }
      Unsafe.unsafe(implicit u => runtime.unsafe.run(ZIO.scoped(zio)).getOrThrow())
    }

    "be able to accept behavior using different ReloadBehaviour" in new mocks {
      val function = (s: String) => ZIO.succeed(s"${s}_changed")

      val stop = mock[UStoppingService[String, String]]
      when(stop.stop(any())).thenAnswer((invocation: InvocationOnMock) =>
        ZIO.succeed(invocation.getArgument[String](0))
      )

      val restart = mock[URestartingService[String, String]]
      when(restart.restart(any(), any())).thenAnswer((invocation: InvocationOnMock) =>
        function(invocation.getArgument[String](0))
      )

      val zio = for {
        (initialReloadable, updater) <- Reloadable.root("initial")
        mappedReloadable1            <- initialReloadable.mapF(function, Stop((s: String) => stop.stop(s).unit))
        mappedReloadable2 <- initialReloadable.mapF(
          function,
          Restart[UIO, String, String]((a, b) => restart.restart(a, b), _ => ZIO.unit)
        )
        _        <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater.compose(_.toString())).fork
        initial1 <- mappedReloadable1.get
        initial2 <- mappedReloadable2.get
        _        <- ZIO.sleep(3 seconds)
        _ = verify(stop).stop("initial_changed")
        _ = verify(restart).restart("0", "initial_changed")
        first1  <- mappedReloadable1.get
        first2  <- mappedReloadable2.get
        _       <- ZIO.sleep(3 seconds)
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
      Unsafe.unsafe(implicit u => runtime.unsafe.run(ZIO.scoped(zio)).getOrThrow())
    }

    "be able to change its reload behaviour to dirty computation in pure way using mapF" in new mocks {
      val function = (s: String) => ZIO.succeed(s"${s}_changed")

      val zio =
        (for {
          (reloadable, updater) <- Reloadable.root("initial")
          mappedReloadable      <- reloadable.mapF[Any, Nothing, String](function)
          _                     <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater.compose(_.toString())).fork
          initial               <- mappedReloadable.get
          _                     <- ZIO.sleep(3 seconds)
          first                 <- mappedReloadable.get
          _                     <- ZIO.sleep(3 seconds)
          second                <- mappedReloadable.get
        } yield {
          initial shouldBe "initial_changed"
          first shouldBe "0_changed"
          second shouldBe "1_changed"
        }).timeout(7 seconds)
      Unsafe.unsafe(implicit u => runtime.unsafe.run(ZIO.scoped(zio)).getOrThrow())
    }

    "be able to produce new reloadable by combining sources of this and that reloadables " in new mocks {
      val function = (s: String, i: Int) => Data(s, i)

      val zio = for {
        (initialReloadable1, updater1) <- Reloadable.root("initial")
        (initialReloadable2, updater2) <- Reloadable.root(-1)
        combinedReloadable             <- initialReloadable1.combine(initialReloadable2)(function)
        _       <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater1.compose(_.toString())).fork
        _       <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater2).fork
        initial <- combinedReloadable.get
        _       <- ZIO.sleep(3 seconds)
        first   <- combinedReloadable.get
        _       <- ZIO.sleep(3 seconds)
        second  <- combinedReloadable.get
      } yield {
        initial shouldBe Data("initial", -1)
        first shouldBe Data("0", 0)
        second shouldBe Data("1", 1)
      }

      Unsafe.unsafe(implicit u => runtime.unsafe.run(ZIO.scoped(zio)).getOrThrow())
    }

    "be able to produce new reloadable by combining sources of this and that reloadables with dirty function " in new mocks {
      val function = (s: String, i: Int) => ZIO.succeed(Data(s, i))

      val zio = (for {
        (initialReloadable1, updater1) <- Reloadable.root("initial")
        (initialReloadable2, updater2) <- Reloadable.root(-1)
        combinedReloadable             <- initialReloadable1.combineF(initialReloadable2)(function)
        _       <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater1.compose(_.toString())).fork
        _       <- intervalAtFixedRate(2 seconds, 2 seconds, 2)(updater2).fork
        initial <- combinedReloadable.get
        _       <- ZIO.sleep(3 seconds)
        first   <- combinedReloadable.get
        _       <- ZIO.sleep(3 seconds)
        second  <- combinedReloadable.get
      } yield {
        initial shouldBe Data("initial", -1)
        first shouldBe Data("0", 0)
        second shouldBe Data("1", 1)
      }).timeout(7 seconds)
      Unsafe.unsafe(implicit u => runtime.unsafe.run(ZIO.scoped(zio)).getOrThrow())
    }

    "survive after errors in stop handler" in new mocks {
      val stop = mock[StoppingService[Any, String, String]]
      when(stop.stop(any())).thenReturn(
        ZIO.fail(exception),
        ZIO.fail(exception),
        ZIO.succeed("stopped")
      )

      val zio =
        ZIO.scoped(for {
          (reloadable, updater) <- Reloadable.root("initial")
          mappedReloadable      <- reloadable.mapF(s => ZIO.succeed(s * 2), Stop((s: String) => stop.stop(s).unit))
          _                     <- intervalAtFixedRate(1 second, 1 second, 10)(updater.compose(_.toString)).fork
          _                     <- ZIO.sleep(3500 millis)
          cur                   <- mappedReloadable.get
        } yield {
          verify(stop, times(3)).stop(any())
          cur shouldBe "22"
        }) *> ZIO.sleep(1500 millis)

      Unsafe.unsafe(implicit u => runtime.unsafe.run(zio).getOrThrow())
      verify(stop, times(4)).stop(any())
    }

    "survive after errors in restart handler" in new mocks {
      val restart      = mock[RestartingService[String, String]]
      var counter: Int = 0
      when(restart.restart(any(), any())).thenAnswer { (invocation: InvocationOnMock) =>
        counter += 1
        if (counter == 2) {
          ZIO.fail(exception)
        } else {
          ZIO.succeed(invocation.getArgument[String](0) + invocation.getArgument[String](1))
        }
      }

      val zio =
        ZIO.scoped(for {
          (reloadable, updater) <- Reloadable.root("initial")
          mappedReloadable <- reloadable.mapF(
            s => ZIO.succeed(s * 2),
            Restart((a, b) => restart.restart(a, b), (_: String) => ZIO.unit)
          )
          _   <- intervalAtFixedRate(1 second, 1 second, 10)(updater.compose(_.toString())).fork
          _   <- ZIO.sleep(3500 millis)
          cur <- mappedReloadable.get
          _ = verify(restart, times(3)).restart(any(), any())
          _ = verify(restart).restart("0", "initialinitial")
          _ = verify(restart).restart("1", "0initialinitial")
          _ = verify(restart).restart("2", "0initialinitial")
        } yield cur shouldBe "20initialinitial") *> ZIO.sleep(1500 millis)

      Unsafe.unsafe(implicit u => runtime.unsafe.run(zio).getOrThrow())
      verify(restart, times(3)).restart(any(), any())
    }

    "don't stop observable even for long stop operations" in new mocks {
      val stop = mock[StoppingService[Clock, String, String]]
      when(stop.stop(any())).thenReturn(ZIO.sleep(5.seconds).as(""))
      val zio = for {
        (initialReloadable, updater) <- Reloadable.root("initial")
        doubleF <- initialReloadable.mapF(s => ZIO.succeed(s * 2), Stop((s: String) => stop.stop(s).unit))
        maskF   <- doubleF.mapF(s => ZIO.succeed(s.take(1) + "***"), Stop((s: String) => stop.stop(s).unit))
        _       <- intervalAtFixedRate(1 second, 1 second, 10)(updater.compose(_.toString())).fork
        _       <- ZIO.sleep(1100 millis)
        double1 <- doubleF.get
        mask1   <- maskF.get

        _       <- ZIO.sleep(1 second)
        double2 <- doubleF.get
        mask2   <- maskF.get
      } yield {
        double1 shouldBe "00"
        mask1 shouldBe "0***"
        double2 shouldBe "11"
        mask2 shouldBe "1***"
      }

      Unsafe.unsafe(implicit u => runtime.unsafe.run(ZIO.scoped(zio)).getOrThrow())
    }

    "do side effects using forEachF" in new mocks {
      val buffer = mutable.ListBuffer[String]()
      val zio = Reloadable
        .root("initial")
        .flatMap { case (reloadable, updater) =>
          reloadable.mapF[Any, Nothing, String](s => ZIO.succeed(s * 2)) <*> ZIO.succeed(updater)
        }
        .flatMap { case (mappedReloadable, updater) =>
          intervalAtFixedRate(1 second, 1 second, 10)(updater.compose(_.toString())).fork *>
            mappedReloadable.forEachF(s => ZIO.succeed(buffer += s))
        }
        .timeout(2500 millis)

      Unsafe.unsafe(implicit u => runtime.unsafe.run(ZIO.scoped(zio)).getOrThrow())
      buffer.toList shouldBe List("initialinitial", "00", "11")
    }

    "filter duplicated keys" in new mocks {
      val buffer1 = mutable.ListBuffer[Int]()
      val buffer2 = mutable.ListBuffer[Int]()
      val zio = (for {
        (initialReloadable, updater) <- Reloadable.root(0)
        filteredR                    <- initialReloadable.distinctByKey(i => i * i * i)
        absR                         <- filteredR.map(_.abs)
        filtered2R                   <- absR.distinctByKey(identity)
        _      <- delayedList(List(1, 1, -1, -1, 2, 2, 2, 1, 1, -1, -1, 3, -3), 1 second)(updater).fork
        fiber1 <- filteredR.forEachF(i => ZIO.succeed(buffer1 += i)).fork
        fiber2 <- filtered2R.forEachF(i => ZIO.succeed(buffer2 += i)).fork
        _      <- fiber1.join
        _      <- fiber2.join
      } yield ()).timeout(15 seconds)

      Unsafe.unsafe(implicit u => runtime.unsafe.run(ZIO.scoped(zio)).getOrThrow())
      buffer1.toList shouldBe List(0, 1, -1, 2, 1, -1, 3, -3)
      buffer2.toList shouldBe List(0, 1, 2, 1, 3)
    }

    "handle slow and fast mapped reloadables" in new mocks {
      val zio = for {
        (initialReloadable, updater) <- Reloadable.root(0)
        fast1Reloadable              <- initialReloadable.map(_ + 1)
        fast2Reloadable              <- initialReloadable.map(_ * 2)
        slowReloadable               <- fast2Reloadable.mapF[Clock, Nothing, Int](i => ZIO.sleep(1 second).as(i + 1))
        _                            <- delayedList(List(1, 2, 3, 4), 0 second)(updater).fork
        _                            <- ZIO.sleep(2500 millis)
        plusOne1                     <- fast1Reloadable.get
        doubled1                     <- fast2Reloadable.get
        doubledPlusOne1              <- slowReloadable.get

        _               <- ZIO.sleep(1 second)
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

      Unsafe.unsafe(implicit u => runtime.unsafe.run(ZIO.scoped(zio)).getOrThrow())
    }

    "handle slow and fast combined reloadables" in new mocks {
      val zio = for {
        (initialReloadable1, updater1) <- Reloadable.root(0)
        (initialReloadable2, updater2) <- Reloadable.root(0)

        fastMappedReloadable <- initialReloadable1.map(_ + 1)
        slowMappedReloadable <- initialReloadable2.mapF[Clock, Nothing, Int](i => ZIO.sleep(1 second).as(i - 1))

        combined <- fastMappedReloadable.combineF(slowMappedReloadable)((a, b) => ZIO.sleep(2 seconds).as((a, b)))
        _        <- delayedList(List(1, 2, 3, 4), 0 second)(updater1).fork
        _        <- delayedList(List(-1, -2, -3, -4), 0 second)(updater2).fork

        _      <- ZIO.sleep(2500 millis)
        value1 <- combined.get

        _      <- ZIO.sleep(2 seconds)
        value2 <- combined.get

        _      <- ZIO.sleep(2 seconds)
        value3 <- combined.get
      } yield {
        value1 shouldBe (2, -1)
        value2 shouldBe (2, -2)
        value3 shouldBe (3, -2)
      }

      Unsafe.unsafe(implicit u => runtime.unsafe.run(ZIO.scoped(zio)).getOrThrow())
    }

    "combine several reloadables" in new mocks {
      case class Data(s: String, i: Int, b: Boolean, d: Double)

      val zio = (for {
        (initialReloadable1, updater1) <- Reloadable.root("initial")
        (initialReloadable2, updater2) <- Reloadable.root(-1)
        (initialReloadable3, updater3) <- Reloadable.root(true)
        (initialReloadable4, updater4) <- Reloadable.root(-1d)
        combinedReloadable <- Reloadable.combine(
          initialReloadable1,
          initialReloadable2,
          initialReloadable3,
          initialReloadable4
        )(Data.apply _)
        _       <- intervalAtFixedRate(1 seconds, 1 seconds, 7)(updater1.compose(_.toString())).fork
        _       <- intervalAtFixedRate(1 seconds, 1 seconds, 7)(updater2).fork
        _       <- intervalAtFixedRate(1 seconds, 1 seconds, 7)(updater3.compose(_ % 2 == 0)).fork
        _       <- intervalAtFixedRate(1 seconds, 1 seconds, 7)(updater4.compose(_.toDouble)).fork
        initial <- combinedReloadable.get
        _       <- ZIO.sleep(3 seconds)
        first   <- combinedReloadable.get
        _       <- ZIO.sleep(3 seconds)
        second  <- combinedReloadable.get
      } yield {
        initial shouldBe Data("initial", -1, true, -1d)
        first shouldBe Data("1", 1, false, 1d)
        second shouldBe Data("4", 4, true, 4d)
      }).timeout(7 seconds)

      Unsafe.unsafe(implicit u => runtime.unsafe.run(ZIO.scoped(zio)).getOrThrow())
    }

    "map with managed" in new mocks {
      val zio = Ref
        .make[List[String]](Nil).tap { closedRef =>
          ZIO.scoped(
            for {
              (reloadable, updater) <- Reloadable.root("initial")
              mappedReloadable <- reloadable.mapScoped { str =>
                ZIO.acquireRelease(ZIO.succeed(str.toUpperCase))(value => closedRef.update(value :: _))
              }
              _            <- intervalAtFixedRate(1 seconds, 1 seconds, 2)(updater.compose(_.toString())).fork
              initial      <- mappedReloadable.get
              _            <- ZIO.sleep(1500 millis)
              second       <- mappedReloadable.get
              secondClosed <- closedRef.get
              _            <- ZIO.sleep(1 second)
              third        <- mappedReloadable.get
              thirdClosed  <- closedRef.get
            } yield {
              initial shouldBe "INITIAL"
              second shouldBe "0"
              third shouldBe "1"
              secondClosed shouldBe List("INITIAL")
              thirdClosed shouldBe List("0", "INITIAL")
            }
          )
        }.flatMap(_.get).map { closed =>
          closed shouldBe List("1", "0", "INITIAL")
        }

      Unsafe.unsafe(implicit u => runtime.unsafe.run(ZIO.scoped(zio)).getOrThrow())
    }
  }
}
