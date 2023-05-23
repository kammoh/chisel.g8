import mill._
import mill.scalalib._
import scalafmt._
import \$ivy.`io.crashbox::mill-jnilib:0.3.0`
import \$ivy.`com.lihaoyi::mill-contrib-bloop:\$MILL_VERSION`

object $name;format="camel"$ extends ScalaModule {

  def scalaVersion = "2.13.14"

  val chiselOrg           = "edu.berkeley.cs" // "org.chipsalliance"
  val chiselVersion       = sys.env.getOrElse("CHISEL_VERSION", "3.6-SNAPSHOT")
  val chiselTestVersion   = "0" + chiselVersion.tail
  val chiselVerifyVersion = "0.3.0"

  override def scalacOptions = Seq(
    "-language:reflectiveCalls", "-deprecation", "-feature", "-Xcheckinit",
    "-Wconf:cat=deprecation&msg=Importing from firrtl is deprecated:s",
    "-Wconf:cat=deprecation&msg=will not be supported as part of the migration to the MLIR-based FIRRTL Compiler:s",
  )

  override def repositoriesTask = T.task {
    import coursier.maven.MavenRepository

    super.repositoriesTask() ++ Seq( //
      MavenRepository("https://oss.sonatype.org/content/repositories/releases"),
      MavenRepository("https://oss.sonatype.org/content/repositories/snapshots")
    )
  }

  override def ivyDeps = Agg(
    ivy"\${chiselOrg}::chisel3:\${chiselVersion}",
    // some other useful libs
    ivy"com.lihaoyi::mainargs:0.5.0",
    ivy"com.lihaoyi::pprint:0.8.1",
    ivy"com.lihaoyi::upickle:3.1.0",
    ivy"com.outr::scribe:3.11.1",
  )

  override def scalacPluginIvyDeps = Agg(
    ivy"\${chiselOrg}:::chisel3-plugin:\${chiselVersion}"
  )

  object test extends ScalaTests with TestModule.ScalaTest with TestModule.Munit {
    override def ivyDeps = super.ivyDeps() ++ Agg(
      ivy"org.scalatest::scalatest:3.2.15",
      ivy"org.scalacheck::scalacheck:1.17.0",
      ivy"edu.berkeley.cs::chiseltest:\${chiselTestVersion}"
        .excludeName("scalatest")
        .excludeName("chisel3")
        .excludeName("chisel3-plugin"),
      ivy"org.scalameta::munit::1.0.1",
    )
  }
}
