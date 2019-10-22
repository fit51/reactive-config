package com.github.fit51.reactiveconfig

package object reloadable {

  /**
    * User friendly Reloadable for describing final piece of configuration
    * @tparam F [_] reloading effect
    * @tparam A value of reloadable, that updates
    */
  type Reloadable[F[_], A] = ReloadableInternal[F, _, A]

}
