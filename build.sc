import mill._
import mill.scalalib._

object g8 extends Giter8Module {
}

/** Just a placeholder module to ensure that Scala Steward detects these and
  * then updates them in the actual template.
  */
object Steward extends ScalaModule {
  def scalaVersion = "2.13.15"
  def ivyDeps = Agg(
    ivy"org.scalameta::munit::1.0.3"
  )
}
