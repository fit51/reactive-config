package com.github.fit51.reactiveconfig.parser

import org.scalatest.{Matchers, WordSpecLike}
import org.scalatest.mockito.MockitoSugar
import io.circe.generic.auto._

class CirceConfigDecoderTest extends WordSpecLike with Matchers with MockitoSugar {
  import CirceConfigDecoderTest._

  trait mocks {
    val param = SimpleConfigParameter[String]("value")
    val params = ComplexConfigParameter[String](
      List(SimpleConfigParameter[String]("value1"), SimpleConfigParameter[String]("value2")))

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
        .decoder[SimpleConfigParameter[String]]
        .decode(CirceConfigParser.parser.parse(json).get)
        .get
        .shouldEqual(param)

      CirceConfigDecoder
        .decoder[ComplexConfigParameter[String]]
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
