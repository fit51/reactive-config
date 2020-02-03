package com.github.fit51.reactiveconfig.etcd

import monix.eval.{Task, TaskLift}
import cats.data.NonEmptySet
import cats.implicits._
import com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue
import org.mockito.Mockito.when
import org.mockito.ArgumentMatchers.any
import com.github.fit51.reactiveconfig.parser.ConfigParser
import monix.reactive.Observable
import org.scalatest.{Matchers, WordSpecLike}
import org.scalatestplus.mockito.MockitoSugar

import scala.util.Try
import scala.concurrent.duration._

//Does not work with Java 11, due to Mockito issue, use Java 8
class ReactiveConfigEtcdTest extends WordSpecLike with Matchers with MockitoSugar {
  import monix.execution.Scheduler.Implicits.global

  trait ParsedData
  implicit val configParser = new ConfigParser[ParsedData] {
    override def parse(rawData: String): Try[ParsedData] = Try(???)
  }
  class EtcdClientTask(m: ChannelManager) extends EtcdClient[Task](m) with Watch[Task] {
    override implicit def taskLift: TaskLift[Task] = implicitly[TaskLift[Task]]
  }

  val intersectPrefixes1 = NonEmptySet.of("", "any.other")
  val intersectPrefixes2 = NonEmptySet.of("some.one", "some")

  val okPrefixes1 = NonEmptySet.of("some", "other")
  val okPrefixes2 = NonEmptySet.of("some.one", "some.two")

  "ReactiveConfigEtcd" should {
    "check prefixes" in {
      ReactiveConfigEtcd.doIntersect(intersectPrefixes1) shouldBe true
      ReactiveConfigEtcd.doIntersect(intersectPrefixes2) shouldBe true
      ReactiveConfigEtcd.doIntersect(okPrefixes1) shouldBe false
      ReactiveConfigEtcd.doIntersect(okPrefixes2) shouldBe false
    }

    "fail apply if prefixes intersect and vice versa" in {

      val client = mock[EtcdClientTask]
      when(client.getRecursiveSinceRevision(any(), any(), any())).thenReturn(Task.pure((Seq.empty[KeyValue], 0L)))
      when(client.watch(any())).thenReturn(Task.pure(Observable.empty))

      val etcdConfigFail =
        ReactiveConfigEtcd[Task, ParsedData](client, intersectPrefixes1).redeem(_ => None, _ => Some(Unit))
      etcdConfigFail.runSyncUnsafe(10 seconds) shouldBe None

      val etcdConfigOk = ReactiveConfigEtcd[Task, ParsedData](client, okPrefixes1).redeem(_ => None, _ => Some(Unit))
      etcdConfigOk.runSyncUnsafe(10 seconds) shouldBe Some(Unit)
    }
  }

}
