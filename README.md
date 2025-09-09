# chisel.g8

A minimal [Chisel] 7.x template [Giter8](http://www.foundweekends.org/giter8/) template using [Mill](https://mill-build.org/mill/index.html) and
based on [com-lihaoyi/mill-scala-hello.g8](https://github.com/com-lihaoyi/mill-scala-hello.g8).

## Getting Started

### Usage with Giter8

```sh
g8 kammoh/chisel.g8
```

### Usage with Mill

```sh
mill init kammoh/chisel.g8
```

To specify output directory and project name, use the `-o` and `--name` options:

```sh
mill init kammoh/chisel.g8 -o example_project --name=example
```

This will create a new directory named `example_project` with the project name set to `example`.

### Troubleshooting
When using `mill init`, if you encounter `mill.scalalib.giter8.Giter8Module.init os.SubprocessException` error, try specifying the project name with the `--name` option as shown above to skip the interactive prompt.
