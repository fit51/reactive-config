package com.github.fit51.reactiveconfig.ce.generic

import cats.Parallel
import cats.effect.Concurrent
import cats.effect.IO
import cats.effect.Resource
import com.github.fit51.reactiveconfig.Sensitive
import com.github.fit51.reactiveconfig.ce.config.ReactiveConfig
import com.github.fit51.reactiveconfig.ce.generic.instances._
import com.github.fit51.reactiveconfig.ce.reloadable.Reloadable
import com.github.fit51.reactiveconfig.generic.source
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import org.scalatest.{Matchers, WordSpecLike}

class MacroTest extends WordSpecLike with Matchers {

  val config =
    ReactiveConfig.const[IO](
      Map(
        "test.field1"                -> "42",
        "test.field2"                -> "false",
        "test.field3"                -> "1.5",
        "completely.different.field" -> "2.5",
        "test.password"              -> "password",
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
        "enormous.field16"           -> "-1:true:0",
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
        "enormous.field29"           -> "29"
      )
    )

  implicit val sensitiveDecoder = Decoders.sensitiveDecoder

  "ReloadableMacro" should {
    "generate Reloadable for tiny case classes" in {
      Tiny.reloadable[IO](config).use(_.get).unsafeRunSync() shouldBe Tiny(42)
    }

    "generate Reloadable for medium case classes" in {
      val expected = Plain(42, false, 1.5)
      Plain.reloadable[IO](config).use(_.get).unsafeRunSync() shouldBe expected
      expected.someMethod(1) shouldBe 43
    }

    "generate Reloadable for case classes with prefix" in {
      val expected = NotSoPlain(42, false, 2.5)
      NotSoPlain.reloadable[IO](config).use(_.get).unsafeRunSync() shouldBe expected
    }

    "generate Reloadable for huge case classes" in {
      Enormous.reloadable[IO](config).use(_.get).unsafeRunSync() shouldBe Enormous(
        0,
        1,
        2,
        3,
        4,
        true,
        6,
        7,
        8,
        9,
        10,
        11,
        12,
        13,
        14,
        15,
        Plain(-1, true, 0),
        17,
        18,
        19,
        20,
        21,
        22,
        23,
        24,
        25,
        26,
        27,
        28,
        29
      )
    }

    "generate sensive Reloadable" in {
      WithSensitiveField.reloadable[IO](config).use(_.get).unsafeRunSync() shouldBe WithSensitiveField(
        42,
        Sensitive("passwordpassword")
      )
    }

    "not compile for empty case classes" in {
      assertDoesNotCompile("""
        case class Test()

        object Test {
          def reloadable[F[_]: FlatMap](config: CombinedConfig[F]) = deriveReloadable[F, Test](config)
        }
      """)
    }

    "not compile when there is missing annotation on member" in {
      assertDoesNotCompile("""
        case class Test(
          @source("path")
          field1: Int,
          field2: Boolean
        )

        object Test {
          def reloadable[F[_]: FlatMap](config: CombinedConfig[F]) = deriveReloadable[F, Test](config)
        }
      """)
    }

    "not compile when there is no implicit decoder" in {
      assertDoesNotCompile("""
        final case class Test0(b: Boolean)

        final case class Test(
          @source("path")
          test0: Test0
        )

        object Test {
          def reloadable[F[_]: FlatMap](config: CombinedConfig[F]) = deriveReloadable[F, Test](config)
        }
      """)
    }
  }
}

final case class Tiny(
    @source("test.field1")
    val field1: Int
)

object Tiny {
  import Decoders._

  def reloadable[F[_]: Concurrent: Parallel](config: ReactiveConfig[F, String]) =
    deriveReloadable[F, String, Tiny](config)
}

sealed trait SealedTrait {
  def field1: Int
}

trait NotSealedTrait {
  def field2: Boolean
}

final case class Plain(
    @source("test.field1")
    override val field1: Int,
    @source("test.field2")
    override val field2: Boolean,
    @source("test.field3")
    field3: Double
) extends SealedTrait
    with NotSealedTrait {

  def someMethod(arg1: Int): Int =
    field1 + arg1
}

object Plain {
  import Decoders._

  def reloadable[F[_]: Concurrent: Parallel](config: ReactiveConfig[F, String]) =
    deriveReloadable[F, String, Plain](config)
}

final case class NotSoPlain(
    @source("field1")
    override val field1: Int,
    @source("field2")
    override val field2: Boolean,
    @source("//completely.different.field")
    field3: Double
) extends SealedTrait
    with NotSealedTrait

object NotSoPlain {
  import Decoders._

  def reloadable[F[_]: Concurrent: Parallel](config: ReactiveConfig[F, String]) =
    deriveReloadable[F, String, NotSoPlain](config, "test")
}

final case class Enormous(
    @source("enormous.field0") field0: Int,
    @source("enormous.field1") field1: Int,
    @source("enormous.field2") field2: Int,
    @source("enormous.field3") field3: Int,
    @source("enormous.field4") field4: Int,
    @source("enormous.field5") field5: Boolean,
    @source("enormous.field6") field6: Int,
    @source("enormous.field7") field7: Int,
    @source("enormous.field8") field8: Int,
    @source("enormous.field9") field9: Int,
    @source("enormous.field10") field10: Int,
    @source("enormous.field11") field11: Int,
    @source("enormous.field12") field12: Int,
    @source("enormous.field13") field13: Int,
    @source("enormous.field14") field14: Int,
    @source("enormous.field15") field15: Int,
    @source("enormous.field16") field16: Plain,
    @source("enormous.field17") field17: Int,
    @source("enormous.field18") field18: Int,
    @source("enormous.field19") field19: Int,
    @source("enormous.field20") field20: Int,
    @source("enormous.field21") field21: Int,
    @source("enormous.field22") field22: Int,
    @source("enormous.field23") field23: Int,
    @source("enormous.field24") field24: Int,
    @source("enormous.field25") field25: Int,
    @source("enormous.field26") field26: Int,
    @source("enormous.field27") field27: Int,
    @source("enormous.field28") field28: Int,
    @source("enormous.field29") field29: Int
) {

  def total: Int =
    field0 + field1 + field2 + field3 + field4
}

object Enormous {
  import Decoders._

  def reloadable[F[_]: Concurrent: Parallel](config: ReactiveConfig[F, String]): Resource[F, Reloadable[F, Enormous]] =
    deriveReloadable[F, String, Enormous](config)
}

final case class WithSensitiveField(
    @source("test.field1")
    i: Int,
    @source("test.password")
    password: Sensitive
)

object WithSensitiveField {
  import Decoders._

  def reloadable[F[_]: Concurrent: Parallel](config: ReactiveConfig[F, String])(implicit
      decoder: ConfigDecoder[Sensitive, String]
  ) =
    deriveSensitiveReloadable[F, String, WithSensitiveField](config)(sensitiveDecoder, implicitly, implicitly)
}
