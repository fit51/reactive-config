package com.github.fit51.reactiveconfig.ce.generic

import cats.~>
import cats.Id
import cats.effect.IO
import com.github.fit51.reactiveconfig.Sensitive
import com.github.fit51.reactiveconfig.ce.config.ReactiveConfig
import com.github.fit51.reactiveconfig.ce.generic.Decoders._
import com.github.fit51.reactiveconfig.ce.generic.instances._
import com.github.fit51.reactiveconfig.generic.source
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import org.scalatest.{Matchers, WordSpecLike}

class ReactiveConfigMacroTest extends WordSpecLike with Matchers {

  implicit val ioToId           = λ[IO ~> Id](_.unsafeRunSync())
  implicit val sensitiveDecoder = Decoders.sensitiveDecoder

  val config =
    ReactiveConfig.const[IO](
      Map(
        "test.field1"                -> "42",
        "test.field2"                -> "false",
        "test.field3"                -> "1.5",
        "completely.different.field" -> "2.5",
        "enormous.field0"            -> "0",
        "enormous.field1"            -> "1",
        "enormous.field2"            -> "2",
        "enormous.field3"            -> "3",
        "enormous.field4"            -> "4",
        "enormous.field5"            -> "true",
        "enormous.field6"            -> "6",
        "enormous.field7"            -> "7",
        "enormous.field8"            -> "8",
        "enormous.field9"            -> "9",
        "enormous.field10"           -> "10",
        "enormous.field11"           -> "11",
        "enormous.field12"           -> "12",
        "enormous.field13"           -> "13",
        "enormous.field14"           -> "14",
        "enormous.field15"           -> "15",
        "enormous.field16"           -> "16",
        "enormous.field17"           -> "17",
        "enormous.field18"           -> "18",
        "enormous.field19"           -> "19",
        "enormous.field20"           -> "20",
        "enormous.field21"           -> "21",
        "enormous.field22"           -> "22",
        "enormous.field23"           -> "23",
        "enormous.field24"           -> "24",
        "enormous.field25"           -> "25",
        "enormous.field26"           -> "26",
        "enormous.field27"           -> "27",
        "enormous.field28"           -> "28",
        "enormous.field29"           -> "29",
        "prefix.booleanFlag"         -> "true",
        "prefix.creds"               -> "user:pass"
      )
    )

  "ReactiveConfig" should {
    "generate Reloadable for case classes with prefix" in {
      val expected = NotSoPlain2(42, false, 2.5)
      NotSoPlain2.reloadable[IO](config).use(_.get).unsafeRunSync() shouldBe expected
    }

    "generate Reloadable for huge case classes" in {
      Enormous2.reloadable[IO](config).use(_.get).unsafeRunSync() shouldBe Enormous2(
        0, 1, 2, 3, 4, true, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29
      )
    }

    "generate Volatile for huge case classes" in {
      val expected = Enormous2(
        0, 1, 2, 3, 4, true, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29
      )
      Enormous2.volatile[IO, Id](config).use(r => IO.delay(r.get)).unsafeRunSync() shouldBe expected
      expected.total shouldBe 10
    }

    "generate Reloadable for sensitive class" in {
      val expected = Config(SensitiveConfig("user", Sensitive("passpass")), true)
      Config.reloadable[IO](config).use(_.get).unsafeRunSync() shouldBe expected
    }

    "generate Volatile for sensitive class" in {
      val expected = Config(SensitiveConfig("user", Sensitive("passpass")), true)
      Config.volatile[IO, cats.Id](config).use(r => IO.delay(r.get)).unsafeRunSync() shouldBe expected
    }

    "not compile for empty case classes" in {
      assertDoesNotCompile("""
        @reactiveconfig[String]("prefix")
        final case class Test()
      """)
    }

    "not compile when there is missing annotation on member" in {
      assertDoesNotCompile("""
        @reactiveconfig[String]()
        final case class Test(
          @source("path")
          field1: Int,
          field2: Boolean
        )
      """)
    }

    "not compile when there is no implicit decoder" in {
      assertDoesNotCompile("""
        final case class Test0(b: Boolean)
        @reactiveconfig[String]
        final case class Test(
          @source("path")
          test0: Test0
        )
      """)
    }
  }
}

final case class SensitiveConfig(
    username: String,
    password: Sensitive
)

@reactiveconfig[String]("prefix", true)
final case class Config(
    @source("creds")
    credentials: SensitiveConfig,
    @source("booleanFlag")
    anotherFlag: Boolean
)

object Config {

  implicit def sensitiveConfigDecoder(implicit
      sDecoder: ConfigDecoder[Sensitive, String]
  ): ConfigDecoder[SensitiveConfig, String] =
    s => {
      val Array(username, password) = s.split(":")
      sDecoder.decode(password).map(SensitiveConfig(username, _))
    }
}

sealed trait SealedTrait2 {
  def field1: Int
}

trait NotSealedTrait2 {
  def field2: Boolean
}

@reactiveconfig[String]("test")
final case class NotSoPlain2(
    @source("field1")
    override val field1: Int,
    @source("field2")
    override val field2: Boolean,
    @source("//completely.different.field")
    field3: Double
) extends SealedTrait2
    with NotSealedTrait2

object NotSoPlain2 {

  implicit val sDecoder = Decoders.sensitiveDecoder
}

@reactiveconfig[String]("enormous")
final case class Enormous2(
    @source("field0") field0: Int,
    @source("field1") field1: Int,
    @source("field2") field2: Int,
    @source("field3") field3: Int,
    @source("field4") field4: Int,
    @source("field5") field5: Boolean,
    @source("field6") field6: Int,
    @source("field7") field7: Int,
    @source("field8") field8: Int,
    @source("field9") field9: Int,
    @source("field10") field10: Int,
    @source("field11") field11: Int,
    @source("field12") field12: Int,
    @source("field13") field13: Int,
    @source("field14") field14: Int,
    @source("field15") field15: Int,
    @source("field16") field16: Int,
    @source("field17") field17: Int,
    @source("field18") field18: Int,
    @source("field19") field19: Int,
    @source("field20") field20: Int,
    @source("field21") field21: Int,
    @source("field22") field22: Int,
    @source("field23") field23: Int,
    @source("field24") field24: Int,
    @source("field25") field25: Int,
    @source("field26") field26: Int,
    @source("field27") field27: Int,
    @source("field28") field28: Int,
    @source("field29") field29: Int
) {

  def total: Int =
    field0 + field1 + field2 + field3 + field4
}

object Enormous2 {

  implicit val sDecoder = Decoders.sensitiveDecoder
}
