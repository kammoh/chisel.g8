package $package$

import chisel3._

class Adder(val width: Int) extends Module {

  val io = IO(new Bundle {
    val a   = Input(UInt(width.W))
    val b   = Input(UInt(width.W))
    val sum = Output(UInt((width + 1).W))
  })

  io.sum := io.a +& io.b

  assert(io.a +& io.b === io.sum, p"Assert Failed! a=\${io.a} b=\${io.b} sum=\${io.sum}")
}
