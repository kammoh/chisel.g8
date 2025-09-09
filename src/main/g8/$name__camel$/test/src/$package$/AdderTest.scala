package $package$

import chisel3._
import chisel3.simulator._
import org.scalatest.flatspec.AnyFlatSpec

class AdderTest extends AnyFlatSpec with ChiselSim {
  val rand = scala.util.Random

  val width = 8

  "\$width-bit Adder" should "add two numbers correctly" in {
    simulate(new Adder(8)) { c =>
      for (_ <- 0 until 1000) {
        val a = rand.nextLong(1L << width)
        val b = rand.nextLong(1L << width)
        c.io.a.poke(a.U)
        c.io.b.poke(b.U)
        c.clock.step(1)
        val expected = a + b
        val result   = c.io.sum.peek().litValue.toLong
        assert(result == expected, s"Expected \$expected but got \$result for inputs a=\$a, b=\$b")
      }
    }
  }
}