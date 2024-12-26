import mill._
import mill.scalalib._
import scalafmt._

object $name;format="camel"$ extends ScalaModule {

  def scalaVersion = "2.13.15"

  val chiselVersion       = sys.env.getOrElse("CHISEL_VERSION", "7.0.0-M2+241-2d9c7195-SNAPSHOT")
  val chiselTestVersion   = "6.0.0"

  override def scalacOptions = Seq(
    // checks
    "-language:reflectiveCalls",
    "-deprecation",
    "-feature",
    "-Xcheckinit",
    // warnings
    "-Wunused",
    "-Xlint:adapted-args",
    "-Wconf:cat=unused&msg=parameter .* in .* never used:silent"
  )

  override def repositoriesTask = T.task {
    import coursier.maven.MavenRepository

    super.repositoriesTask() ++ Seq( //
      MavenRepository("https://oss.sonatype.org/content/repositories/releases"),
      MavenRepository("https://oss.sonatype.org/content/repositories/snapshots"),
      MavenRepository("https://s01.oss.sonatype.org/content/repositories/releases"),
      MavenRepository("https://s01.oss.sonatype.org/content/repositories/snapshots"),
      MavenRepository(s"file://\${os.home}/.m2/repository"),
      // MavenRepository("https://jitpack.io"),
    )
  }

  override def ivyDeps = Agg(
    ivy"org.chipsalliance::chisel:\${chiselVersion}",
    ivy"com.lihaoyi::mainargs:0.7.6",
  )

  override def scalacPluginIvyDeps = Agg(
    ivy"org.chipsalliance:::chisel-plugin:\${chiselVersion}"
  )

  object test extends ScalaTests with TestModule.ScalaTest {
    override def ivyDeps = super.ivyDeps() ++ Agg(
      ivy"org.scalatest::scalatest:3.2.19",
      ivy"org.scalacheck::scalacheck:1.18.1",
      ivy"edu.berkeley.cs::chiseltest:\${chiselTestVersion}"
        .excludeName("scalatest")
        .excludeName("chisel3")
        .excludeName("chisel3-plugin"),
      ivy"org.scalameta::munit::1.0.3",
    )
  }
}
