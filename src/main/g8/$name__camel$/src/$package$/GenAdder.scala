package $package$

import chisel3._
import scribe.Logging
import mainargs.{main, arg, ParserForMethods, ParserForClass, Leftover, Flag}

object GenAdder extends GenVerilog {
  implicit def configParser = ParserForClass[Config]

  @main
  def run(
    @arg(positional = true, doc = "Name of the Adder")
    className: String = "Adder",
    @arg(positional = true, name = "width", doc = "Input width")
    width:                 Int = 32,
    config:                Config = Config(),
    additionalFirtoolOpts: Leftover[String]
  ) = {

    val packageName = this.getClass().getPackageName()

    val fqn = if (className.startsWith(packageName + ".")) className else packageName + "." + className

    val moduleGen = () => moduleInstance(fqn, width)

    try {
      val (circuit, topModule, outputSuffix, targetDir) = emitSystemVerilogFile(moduleGen(), config, additionalFirtoolOpts.value.toArray)

      val outFile = targetDir.map(os.RelPath(_)).getOrElse(os.pwd) / s"\$topModule\$outputSuffix"

      println(s"firtool generated \${outFile}")

    } catch {
      case e: java.lang.ClassNotFoundException => {
        println(s"[ERROR] Class not found: \${fqn}")
        sys.exit(1)
      }
      case e: java.lang.NoSuchMethodException => {
        println(s"Incorrect constructor signature for \${fqn}")
        sys.exit(1)
      }
    }
    println("DONE")
  }

  def main(args: Array[String]): Unit = {
    ParserForMethods(this).runOrExit(args.toSeq)
  }
}