package $package$

import chisel3._

import firrtl.annotations.DeletedAnnotation
import firrtl.options.TargetDirAnnotation
import firrtl.EmittedVerilogCircuitAnnotation
import firrtl.EmittedVerilogCircuit
import chisel3.stage.DesignAnnotation

case class Config(
  debug:         Boolean = false,
  preserveNames: Boolean = true,
  noRand:        Boolean = false)

trait GenVerilog {
  import scribe.format._
  val myFormatter = formatter"\${brightYellow(string("["))}\$levelColored\${brightYellow(string("]"))} \$messages\$mdc"
  scribe.Logger.root
    .clearHandlers()
    .clearModifiers()
    .withHandler(minimumLevel = Some(scribe.Level.Trace), formatter = myFormatter)
    .replace()

  def moduleInstance(className: String, params: Any*): Module = {
    val clazz           = Class.forName(className)
    val allConstructors = clazz.getConstructors()
    println(s"\$className has \${allConstructors.size} constructor(s):")
    for (constr <- allConstructors) {
      val params = constr.getParameters()
      println(
        s"    \${constr}   (\${params.length} params)"
      )
    }
    val constructor =
      if (allConstructors.length == 1) allConstructors.head else clazz.getConstructor(params.map(_.getClass()): _*)
    constructor.newInstance(params: _*).asInstanceOf[Module]
  }

  def emitSystemVerilogFile(
    module:                => Module,
    config:                Config,
    chiselOpts:            Array[String] = Array.empty,
    additionalFirtoolOpts: Array[String] = Array.empty
  ) = {
    val firtoolOpts = Array(
      s"""-O=\${if (config.debug) "debug" else "release"}""",
      "--lowering-options=disallowLocalVariables,disallowPortDeclSharing",
      "--add-vivado-ram-address-conflict-synthesis-bug-workaround",
      "--dedup", // Deduplicate structurally identical modules
      s"""--preserve-values=\${if (config.preserveNames) "named" else "none"}""",
      "--export-module-hierarchy",
      "--warn-on-unprocessed-annotations",
    ) ++ IfSeq(config.debug)(
      "--disable-opt",
      "--mlir-pretty-debuginfo",
    ).Else(
      "--strip-fir-debug-info", // Disable source fir locator information in output Verilog
      "--strip-debug-info",     // Disable source locator information in output Verilog
    ) ++ IfSeq(config.noRand)(
      "--disable-mem-randomization", // Disable emission of memory randomization code
      "--disable-reg-randomization", // Disable emission of register randomization code
      "--disable-all-randomization",
    ) ++ additionalFirtoolOpts

    val annos = circt.stage.ChiselStage.emitSystemVerilogFile(
      module,
      args = chiselOpts,
      firtoolOpts = firtoolOpts,
    )

    val targetDir = annos.collectFirst {
      _ match {
        case DeletedAnnotation(_, TargetDirAnnotation(directory)) => directory
      }
    }
    val (name, outputSuffix) = annos.collectFirst {
      _ match {
        case EmittedVerilogCircuitAnnotation(EmittedVerilogCircuit(name, _, outputSuffix)) => (name, outputSuffix)
      }
    }.get
    val elaboratedModule = annos.collectFirst {
      _ match {
        case DesignAnnotation(m) => m

        case DeletedAnnotation(_, DesignAnnotation(m)) => m
      }
    }

    (elaboratedModule, name, outputSuffix, targetDir)
  }
}
