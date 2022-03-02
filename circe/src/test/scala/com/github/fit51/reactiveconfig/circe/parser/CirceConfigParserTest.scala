package com.github.fit51.reactiveconfig.circe.parser

import io.circe.Printer
import org.scalatest.{Matchers, WordSpecLike}
import org.scalatestplus.mockito.MockitoSugar

class CirceConfigParserTest extends WordSpecLike with Matchers with MockitoSugar {

  trait mocks {
    val json  = """{"value":"value"}"""
    val jsons = """{"values":[{"value":"value1"},{"value":"value2"}]}"""
  }

  "CirceEncoder" should {
    "successfully parse json string representation" in new mocks {
      CirceConfigParser.parser.parse(json).get.printWith(Printer.noSpaces).shouldEqual(json)
      CirceConfigParser.parser.parse(jsons).get.printWith(Printer.noSpaces).shouldEqual(jsons)
    }
  }
}
