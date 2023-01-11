package com.github.fit51.reactiveconfig.parser

import scala.util.{Success, Try}

trait ConfigParser[ParsedData] {

  /** Parse the given raw data: [[String]] to [[ParsedData]] (Ex. to Json)
    *
    * [[ParsedData]] is stored in internal storage. Then it's serialized using [[ConfigDecoder]].
    *
    * Note: If no parsing or pre-processing is needed, use ConfigParser[String, String]
    */
  def parse(rawData: String): Try[ParsedData]
}

object ConfigParser {
  def apply[ParsedData](implicit p: ConfigParser[ParsedData]): ConfigParser[ParsedData] = p

  def identity: ConfigParser[String] =
    data => Success(data)
}

object ParserOps {
  def parse[ParsedData](rawData: String)(implicit p: ConfigParser[ParsedData]): Try[ParsedData] =
    p.parse(rawData)
}
