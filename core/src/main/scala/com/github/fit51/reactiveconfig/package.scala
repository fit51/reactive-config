package com.github.fit51

package object reactiveconfig {

  /**
    * Pre-parsed data,
    */
  type Parsed

  /**
    * Value stored in internal key->value Map
    */
  case class Value[ParsedData](parsedData: ParsedData, version: Long)

  /**
    * Represents successfully parsed KeyValue
    */
  case class ParsedKeyValue[ParsedData](key: String, value: Value[ParsedData])

  /**
    * Common reactive config exception
    */
  object ReactiveConfigException {
    def apply(key: String, message: String): ReactiveConfigException =
      new ReactiveConfigException(s"$message on key: $key")

    def apply(message: String): ReactiveConfigException =
      new ReactiveConfigException(message)
  }

  class ReactiveConfigException(message: String) extends Exception(message)
}
