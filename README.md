# ReactiveConfig

ReactiveConfig is a configuration library for Scala. 

It provides functionality to get pieces of config, that reload reactive. 
These pieces of configuration are called `Reloadables`. 
You can `combine` and `map` `Reloadables` to construct change-propagation chains.

Library is implemented on top of [Monix](https://github.com/monix/monix) Observable and supports number of configuration back ends:
* [Typesafe-config](https://github.com/lightbend/config) - HOCON file.  
See [reactive-config-typesafe](https://github.com/fit51/reactive-config/tree/master/typesafe/src)
* [Etcd](https://github.com/etcd-io/etcd) - Distributed reliable key-value store, supports KV change notification streaming.  
See [reactive-config-etcd]()

## Quickstart with sbt

```scala
libraryDependencies += "com.github.fit51" %% "reactive-config-core" % "0.0.1"
```

## Examples
See examples [here](https://github.com/fit51/reactive-config/tree/master/examples).

## Note
Library is in development.  
It was originally created in private Bitbucket repository. Now, library is moved to GitHub with most of commits squashed, due to _some_ reasons. 