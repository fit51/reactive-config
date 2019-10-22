package com.github.fit51.reactiveconfig.storage

import monix.reactive.Observable
import com.github.fit51.reactiveconfig.{ParsedKeyValue, Value}
import scala.collection.concurrent.TrieMap

trait ConfigStorage[F[_], ParsedData] {

  /**
    * Load configuration
    *
    * @return key -> values, that contain ParsedData and key-value version
    * @see [[Value]]
    */
  def load(): F[TrieMap[String, Value[ParsedData]]]

  /**
    * Start observing config changes
    *
    * @return stream of changed key-value pairs.
    * @see[[ParsedKeyValue]]
    */
  def watch(): Observable[ParsedKeyValue[ParsedData]]
}
