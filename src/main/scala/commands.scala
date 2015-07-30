/**
 * Created by kring on 2015/7/29.
 */
import sbt.Keys._
import sbt._

object StellarCommands extends AutoPlugin {
  override lazy val projectSettings = Seq(commands += myCommand)
  override def trigger = allRequirements

  lazy val myCommand =
    Command.command("hello") { (state: State) =>
      println("Hi!")
      state
    }
}
