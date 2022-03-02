package com.github.fit51

package object reactiveconfig {

  final case class Sensitive(value: String) {
    override def toString = value
  }

  /** Pre-parsed data,
    */
  type Parsed

  /** Value stored in internal key->value Map
    */
  case class Value[ParsedData](parsedData: ParsedData, version: Long) {

    def isUpdated(newValue: Value[ParsedData]): Boolean =
      parsedData != newValue.parsedData && version < newValue.version
  }

  /** Represents successfully parsed KeyValue
    */
  case class ParsedKeyValue[ParsedData](key: String, value: Value[ParsedData])

  /** Common reactive config exception
    */
  object ReactiveConfigException {

    def unknownKey(key: String): ReactiveConfigException =
      new UnknownKey(key)

    def unableToParse(message: String, cause: Throwable): ReactiveConfigException =
      new UnableToParse(message, cause)
  }

  sealed abstract class ReactiveConfigException(
      message: String,
      cause: Throwable
  ) extends Exception(message, cause)

  class UnknownKey(val key: String) extends ReactiveConfigException(s"Unknown key $key", null)

  class UnableToParse(message: String, cause: Throwable) extends ReactiveConfigException(message, cause)
}
