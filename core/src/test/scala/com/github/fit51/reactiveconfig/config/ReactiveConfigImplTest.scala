package com.github.fit51.reactiveconfig.config

import cats.effect.Clock
import cats.effect.IO
import cats.instances.future.catsStdInstancesForFuture
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.storage.ConfigStorage
import com.github.fit51.reactiveconfig.{ParsedKeyValue, ReactiveConfigException, Value}
import monix.eval.Task
import monix.eval.TaskLift
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable
import org.mockito.Mockito.when
import org.scalatest.{Matchers, WordSpecLike}
import org.scalatestplus.mockito.MockitoSugar

import scala.collection.concurrent.TrieMap
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Success

class ReactiveConfigImplTest extends WordSpecLike with Matchers with MockitoSugar {
  import ReactiveConfigImplTest._

  trait mocks {
    val storage = TrieMap[String, Value[String]](
      "key1" -> Value("value1", 0L),
      "key2" -> Value("value2", 0L),
      "key3" -> Value("value3", 0L)
    )
    val watch: Observable[ParsedKeyValue[String]] =
      Observable.evalDelayed(2 seconds, ParsedKeyValue("key1", Value("value2", 1L)))

    val config = new ReactiveConfigImpl[IO, String](storage, watch)

    implicit val timer = new cats.effect.Timer[IO] {
      override def clock: Clock[IO] = Clock.create
      override def sleep(duration: FiniteDuration): IO[Unit] =
        Task.sleep(duration).to[IO]
    }
  }

  implicit val futureTaskLift: TaskLift[Future] = new TaskLift[Future] {
    override def apply[A](task: Task[A]): Future[A] = task.runToFuture
  }

  "Config" should {
    "return wrapped in Try value by key" in new mocks {
      config.unsafeGet[String]("key1").shouldEqual(Success("value1"))
      intercept[ReactiveConfigException](config.unsafeGet[String]("key0").get).getMessage
        .shouldEqual("Failed to find ValueByKey on key: key0")
    }

    /*
    "return value by key or throw exception " in new mocks {
      config.getOrThrow[String]("key1").shouldEqual("value1")
      intercept[ReactiveConfigException](config.getOrThrow[String]("key0")).getMessage
        .shouldEqual("Failed to find ValueByKey on key: key0")
    }
    */

    "return reloadable" in new mocks {
      (for {
        reloadable <- config.reloadable[String]("key1")
        value1 <- reloadable.get
        _ <- IO.sleep(3.seconds)
        value2 <- reloadable.get
      } yield {
        value1 shouldBe "value1"
        value2 shouldBe "value2"
      }).unsafeRunSync()
    }

    "be able to create itself using any effect and future" in new mocks {
      val configStorageIO = mock[ConfigStorage[IO, String]]

      when(configStorageIO.watch()).thenReturn(IO.pure(watch))
      when(configStorageIO.load()).thenReturn(IO(storage))

      Await.result(ReactiveConfigImpl[IO, String](configStorageIO).unsafeToFuture, 1 second)

      val configStorageTask = mock[ConfigStorage[Task, String]]

      when(configStorageTask.watch()).thenReturn(Task.pure(watch))
      when(configStorageTask.load()).thenReturn(Task(storage))

      Await.result(ReactiveConfigImpl[Task, String](configStorageTask).runToFuture, 1 second)

      val configStorageFuture = mock[ConfigStorage[Future, String]]

      when(configStorageFuture.watch()).thenReturn(Future.successful(watch))
      when(configStorageFuture.load()).thenReturn(Future(storage))

      ReactiveConfigImpl[Future, String](configStorageFuture)
    }
  }
}

object ReactiveConfigImplTest {

  implicit val stringDecoder: ConfigDecoder[String, String] = (parsed: String) => Success(parsed)
}
