# reactive-config

reactive-config is a configuration library for Scala. 

It provides functionality to get pieces of config, that reload. 
The piece of configuration is called `Reloadable`. 
You can `combine` and `map` `Reloadables` to construct change-propagation graphs.

Library is implemented on top of [Monix](https://github.com/monix/monix) Observable and supports number of configuration back ends:
* [Typesafe-config](https://github.com/lightbend/config) - HOCON file.  
See [reactive-config-typesafe](https://github.com/fit51/reactive-config/tree/master/typesafe/src)
* [Etcd](https://coreos.com/etcd/) - Distributed reliable key-value store, that supports monitoring changes to keys.  
See [reactive-config-etcd]()

## Quickstart with sbt

```scala
libraryDependencies += "com.github.fit51" %% "reactive-config-core" % "0.0.1"
```

## Examples
See examples [here](https://github.com/fit51/reactive-config/tree/master/examples).

## Etcd
Run "sbt etcd/generateSources" to generate etcd GRPC services and move them to source directory.

## Note
Library is in development.  
It was originally created in private Bitbucket repository. Now, library is moved to GitHub with most of commits squashed due to _some_ reasons. 
