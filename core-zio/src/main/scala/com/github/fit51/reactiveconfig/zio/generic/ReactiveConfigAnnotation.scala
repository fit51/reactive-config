package com.github.fit51.reactiveconfig.zio.generic

import com.github.fit51.reactiveconfig.generic.CommonConfigAnnotation

import scala.reflect.macros.whitebox

class ReactiveConfigAnnotation(override val c: whitebox.Context) extends CommonConfigAnnotation(c) {
  import c.universe._

  override def reloadableAndVolatile(dataType: Type, tpname: TypeName, prefix: String, sensitive: Boolean): Tree = {
    val name = tpname.toTypeName
    val imports =
      q"""
        import com.github.fit51.reactiveconfig.ReactiveConfigException
        import com.github.fit51.reactiveconfig.zio.config.ReactiveConfig
        import com.github.fit51.reactiveconfig.zio.reloadable.Reloadable
        import zio.Managed
      """
    if (sensitive) {
      q"""
        ..$imports
        import com.github.fit51.reactiveconfig.Sensitive
        import com.github.fit51.reactiveconfig.parser.ConfigDecoder

        def reloadable(config: ReactiveConfig[$dataType])(implicit d: ConfigDecoder[Sensitive, $dataType]): Managed[ReactiveConfigException, Reloadable[$name]] =
          com.github.fit51.reactiveconfig.zio.generic.deriveSensitiveReloadable[$dataType, $name](config, $prefix)
      """
    } else {
      q"""
        ..$imports

        def reloadable(config: ReactiveConfig[$dataType]): Managed[ReactiveConfigException, Reloadable[$name]] =
          com.github.fit51.reactiveconfig.zio.generic.deriveReloadable[$dataType, $name](config, $prefix)
      """
    }
  }
}
