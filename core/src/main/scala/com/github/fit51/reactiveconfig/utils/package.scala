package com.github.fit51.reactiveconfig

import cats.MonadError

package object utils {

  type MonadThrow[F[_]] = MonadError[F, Throwable]

}
