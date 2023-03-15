# reactive-config

reactive-config is a configuration library for Scala.

It provides functionality to get pieces of config, that reload.
The piece of configuration is called `Reloadable`.
You can `combine` and `map` `Reloadables` to construct change-propagation graphs.

Library is implemented for both ZIO and cats-effect stacks and supports number of configuration back ends:
* [Typesafe-config](https://github.com/lightbend/config) - HOCON file
See [typesafe-ce](https://github.com/fit51/reactive-config/tree/master/typesafe-ce) or [typesafe-zio](https://github.com/fit51/reactive-config/tree/master/typesafe-zio)
* [Etcd](https://coreos.com/etcd/) - Distributed reliable key-value store, that supports monitoring changes to keys.
See [etcd-ce](https://github.com/fit51/reactive-config/tree/master/etcd-ce) or [etcd-zio](https://github.com/fit51/reactive-config/tree/master/etcd-zio)

## Quickstart with sbt

```scala
libraryDependencies ++= Seq(
    "com.github.fit51" %% "reactiveconfig-circe" % version
    "com.github.fit51" %% "reactiveconfig-etcd-zio" % version,
    "com.github.fit51" %% "reactiveconfig-typesafe-zio" % version
)
```

## Examples
See examples [here](https://github.com/fit51/reactive-config/tree/master/examples).

## Note
Library is in development.
It was originally created in private Bitbucket repository. Now, library is moved to GitHub with most of commits squashed due to _some_ reasons.
