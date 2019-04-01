package com.github.fit51.reactiveconfig.config

import cats.effect.IO
import cats.instances.future.catsStdInstancesForFuture
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.storage.ConfigStorage
import com.github.fit51.reactiveconfig.{ParsedKeyValue, ReactiveConfigException, Value}
import org.mockito.Mockito.when
import monix.reactive.Observable
import org.scalatest.{Matchers, WordSpecLike}
import monix.eval.Task
import scala.collection.concurrent.TrieMap
import monix.execution.Scheduler.Implicits.global
import org.scalatest.mockito.MockitoSugar
import scala.concurrent.{Await, Future}
import scala.util.Success
import scala.concurrent.duration._

class ReactiveConfigImplTest extends WordSpecLike with Matchers with MockitoSugar {
  import ReactiveConfigImplTest._

  trait mocks {
    val storage = TrieMap[String, Value[String]](
      "key1" -> Value("value1", 0l),
      "key2" -> Value("value2", 0l),
      "key3" -> Value("value3", 0l)
    )
    val watch: Observable[ParsedKeyValue[String]] =
      Observable.evalDelayed(2 seconds, ParsedKeyValue("key1", Value("value2", 1l)))

    val config = new ConfigImpl[IO, String](storage, watch)
  }

  "Config" should {

    "return wrapped in Option value by key" in new mocks {
      config.get[String]("key1").shouldEqual(Some("value1"))
      config.get[String]("key0").shouldEqual(None)
    }

    "return wrapped in Try value by key" in new mocks {
      config.getTry[String]("key1").shouldEqual(Success("value1"))
      intercept[ReactiveConfigException](config.getTry[String]("key0").get).getMessage
        .shouldEqual("Failed to find ValueByKey on key: key0")
    }

    "return value by key or throw exception " in new mocks {
      config.getOrThrow[String]("key1").shouldEqual("value1")
      intercept[ReactiveConfigException](config.getOrThrow[String]("key0")).getMessage
        .shouldEqual("Failed to find ValueByKey on key: key0")
    }

    "return reloadable" in new mocks {
      val reloadable = config.reloadable[String]("key1")
      reloadable.get.shouldEqual("value1")
      Thread.sleep(3000)
      reloadable.get.shouldEqual("value2")
    }

    "be able to create itself using any effect and future" in new mocks {
      val configStorageIO = mock[ConfigStorage[IO, String]]

      when(configStorageIO.watch()).thenReturn(watch)
      when(configStorageIO.load()).thenReturn(IO(storage))

      Await.result(ConfigImpl[IO, String](configStorageIO).unsafeToFuture, 1 second)

      val configStorageTask = mock[ConfigStorage[Task, String]]

      when(configStorageTask.watch()).thenReturn(watch)
      when(configStorageTask.load()).thenReturn(Task(storage))

      Await.result(ConfigImpl[Task, String](configStorageTask).runToFuture, 1 second)

      val configStorageFuture = mock[ConfigStorage[Future, String]]

      when(configStorageFuture.watch()).thenReturn(watch)
      when(configStorageFuture.load()).thenReturn(Future(storage))

      ConfigImpl[Future, String](configStorageFuture)
    }
  }
}

object ReactiveConfigImplTest {

  implicit val stringDecoder: ConfigDecoder[String, String] = (parsed: String) => Success(parsed)
}
