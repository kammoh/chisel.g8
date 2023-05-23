package $package$

import chisel3._
import chiseltest._
import chiseltest.formal.{Formal, BoundedCheck}
import chiseltest.formal.backends._
import org.scalatest.flatspec.AnyFlatSpec

class AdderFormal extends AnyFlatSpec with ChiselScalatestTester with Formal {
  for {
    width <- Seq(3, 4, 5)
  } {

    "Adder" should s"verify formally for width=\${width}" taggedAs FormalTag in {
      verify(
        new Adder(width),
        Seq(BitwuzlaEngineAnnotation, BoundedCheck(20 max (width + 3)))
      )
    }
  }
}

class AdderTest extends AnyFlatSpec with ChiselScalatestTester {

  val rand = new scala.util.Random(0)

  val annos = Seq()

  for (width <- Seq(4, 5, 7)) {
    "Adder" should s"simulate correctly for width=\$width" in {
      test(new Adder(width))
        .withAnnotations(annos ++ Option.when(width > 8)(VerilatorBackendAnnotation)) { dut =>
          for (_ <- (1 to 10_000)) {
            val a = BigInt(width, rand)
            val b = BigInt(width, rand)
            dut.io.a.poke(a)
            dut.io.b.poke(b)
            dut.clock.step(1)
            dut.io.sum.expect(a + b, s"a=\$a b=\$b")
          }
        }
    }
  }
}
