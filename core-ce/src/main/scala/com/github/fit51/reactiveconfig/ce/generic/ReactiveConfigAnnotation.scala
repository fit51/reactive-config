package com.github.fit51.reactiveconfig.ce.generic

import com.github.fit51.reactiveconfig.generic.CommonConfigAnnotation

import scala.reflect.macros.whitebox

private class ReactiveConfigAnnotation(override val c: whitebox.Context) extends CommonConfigAnnotation(c) {
  import c.universe._

  override def reloadableAndVolatile(dataType: Type, tpname: TypeName, prefix: String, sensitive: Boolean): Tree = {
    val name = tpname.toTypeName
    val imports =
      q"""
        import cats.~>
        import cats.Parallel
        import cats.effect.{Concurrent, Resource}
        import com.github.fit51.reactiveconfig.ce.config.ReactiveConfig
        import com.github.fit51.reactiveconfig.ce.reloadable.Reloadable
        import com.github.fit51.reactiveconfig.reloadable.Volatile
      """
    if (sensitive) {
      q"""
        ..$imports
        import com.github.fit51.reactiveconfig.Sensitive
        import com.github.fit51.reactiveconfig.parser.ConfigDecoder

        def reloadable[F[_]: Concurrent: Parallel](config: ReactiveConfig[F, $dataType])(implicit d: ConfigDecoder[Sensitive, $dataType]): Resource[F, Reloadable[F, $name]] =
          com.github.fit51.reactiveconfig.ce.generic.deriveSensitiveReloadable[F, $dataType, $name](config, $prefix)

        def volatile[F[_]: Concurrent: Parallel, G[_]](config: ReactiveConfig[F, $dataType])(implicit ftog: F ~> G, d: ConfigDecoder[Sensitive, $dataType]): Resource[F, Volatile[G, $name]] =
          reloadable[F](config).flatMap(_.makeVolatile(ftog))
      """
    } else {
      q"""
        ..$imports

        def reloadable[F[_]: Concurrent: Parallel](config: ReactiveConfig[F, $dataType]): Resource[F, Reloadable[F, $name]] =
          com.github.fit51.reactiveconfig.ce.generic.deriveReloadable[F, $dataType, $name](config, $prefix)

        def volatile[F[_]: Concurrent: Parallel, G[_]](config: ReactiveConfig[F, $dataType])(implicit ftog: F ~> G): Resource[F, Volatile[G, $name]] =
          reloadable[F](config).flatMap(_.makeVolatile(ftog))
      """
    }
  }
}
