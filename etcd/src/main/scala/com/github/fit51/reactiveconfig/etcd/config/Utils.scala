package com.github.fit51.reactiveconfig.etcd.config

import cats.data.NonEmptySet

object Utils {

  def doIntersect(prefixes: NonEmptySet[String]): Boolean =
    !prefixes.forall { prefix1 =>
      prefixes.forall { prefix2 =>
        prefix1 == prefix2 || !prefix1.startsWith(prefix2)
      }
    }
}
