package $package$

import _root_.circt.stage.ChiselStage

object Main extends App {
  println(
    ChiselStage.emitSystemVerilog(
      gen = new Adder(8),
      firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info", "-default-layer-specialization=enable")
    )
  )
}