package com.github.fit51.reactiveconfig.circe.parser

import io.circe.generic.auto._
import org.scalatest.{Matchers, WordSpecLike}
import org.scalatestplus.mockito.MockitoSugar

class CirceConfigDecoderTest extends WordSpecLike with Matchers with MockitoSugar {
  import CirceConfigDecoderTest._

  trait mocks {
    val param = SimpleConfigParameter[String]("value")
    val params = ComplexConfigParameter[String](
      List(SimpleConfigParameter[String]("value1"), SimpleConfigParameter[String]("value2"))
    )

    val json =
      s"""
         |{
         |  "value": "value"
         |}
         """.stripMargin

    val jsons =
      s"""
         |{
         |  "values": [
         |    {"value": "value1"},
         |    {"value": "value2"}
         |  ]
         |}
         """.stripMargin
  }

  "CirceDecoder" should {

    "successfully decode json" in new mocks {
      CirceConfigDecoder
        .circeConfigDecoder[SimpleConfigParameter[String]]
        .decode(CirceConfigParser.parser.parse(json).get)
        .get
        .shouldEqual(param)

      CirceConfigDecoder
        .circeConfigDecoder[ComplexConfigParameter[String]]
        .decode(CirceConfigParser.parser.parse(jsons).get)
        .get
        .shouldEqual(params)
    }
  }
}

object CirceConfigDecoderTest {

  case class SimpleConfigParameter[T](value: T)

  case class ComplexConfigParameter[T](values: List[SimpleConfigParameter[T]])
}
