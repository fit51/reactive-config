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
        import zio.{Scope, ZIO, ZLayer}
      """
    if (sensitive) {
      q"""
        ..$imports
        import com.github.fit51.reactiveconfig.Sensitive
        import com.github.fit51.reactiveconfig.parser.ConfigDecoder
        import zio.&

        def reloadable(config: ReactiveConfig[$dataType])(implicit d: ConfigDecoder[Sensitive, $dataType]): ZIO[Scope, ReactiveConfigException, Reloadable[$name]] =
          com.github.fit51.reactiveconfig.zio.generic.deriveSensitiveReloadable[$dataType, $name](config, $prefix)

        val layer: ZLayer[ReactiveConfig[$dataType] & ConfigDecoder[Sensitive, $dataType], ReactiveConfigException, Reloadable[$name]] =
          ZLayer.scoped {
            for {
              config <- ZIO.service[ReactiveConfig[$dataType]]
              decoder <- ZIO.service[ConfigDecoder[Sensitive, $dataType]]
              result <- reloadable(config)(decoder)
            } yield result
          }
      """
    } else {
      q"""
        ..$imports

        def reloadable(config: ReactiveConfig[$dataType]): ZIO[Scope, ReactiveConfigException, Reloadable[$name]] =
          com.github.fit51.reactiveconfig.zio.generic.deriveReloadable[$dataType, $name](config, $prefix)

        val layer: ZLayer[ReactiveConfig[$dataType], ReactiveConfigException, Reloadable[$name]] =
          ZLayer.scoped {
            for {
              config <- ZIO.service[ReactiveConfig[$dataType]]
              result <- reloadable(config)
            } yield result
          }
      """
    }
  }
}
