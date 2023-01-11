package com.github.fit51.reactiveconfig.zio.etcd

import cats.data.NonEmptySet
import com.github.fit51.reactiveconfig.etcd.config.Utils
import org.scalatest.{Matchers, WordSpecLike}

class ReactiveEtcdConfigTest extends WordSpecLike with Matchers {

  val intersectPrefixes1 = NonEmptySet.of("", "any.other")
  val intersectPrefixes2 = NonEmptySet.of("some.one", "some")

  val okPrefixes1 = NonEmptySet.of("some", "other")
  val okPrefixes2 = NonEmptySet.of("some.one", "some.two")

  "ReactiveEtcdConfig" should {

    "check prefixes" in {
      Utils.doIntersect(intersectPrefixes1) shouldBe true
      Utils.doIntersect(intersectPrefixes2) shouldBe true
      Utils.doIntersect(okPrefixes1) shouldBe false
      Utils.doIntersect(okPrefixes2) shouldBe false
    }
  }
}
