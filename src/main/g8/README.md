# Chisel Template

A minimal [Chisel](https://www.chisel-lang.org/) using [Mill](https://mill-build.org/mill/index.html) build tool.

To use [bloop](https://scalacenter.github.io/bloop/):

```sh
mill --import ivy:com.lihaoyi::mill-contrib-bloop:  mill.contrib.bloop.Bloop/install
```

To see dependency updates:

```sh
mill mill.scalalib.Dependency/showUpdates --allowPreRelease true
```
