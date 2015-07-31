/**
 * Created by kring on 2015/7/29.
 */
import sbt.Keys._
import sbt._

import org.strllar.stellarbase._

object StellarCommands extends AutoPlugin {
  override lazy val projectSettings = Seq(commands += Command.command("solarEscape")(solarEscapeCmdProc))
  override def trigger = allRequirements

  val helpMessage =
    """
      |                                                             ---]===>
      |                       Welcome the Stellar Network!
      |Available command:
      |  genkey,exit
      |  TODO: init, server, believe, info, tx, send, dumpxdr, help
    """.stripMargin

  def solarEscapeCmdProc(state: State): State = {
    println(helpMessage)
    state.copy(
      definedCommands =
        supportedCommands ++
        Seq(BasicCommands.shell, BasicCommands.exit), //++BuiltinCommands.ConsoleCommands
      remainingCommands =
        state.remainingCommands :+ "shell"
    )
  }

  lazy val supportedCommands = Seq(
    Command.command("genkey")(genkeyCmdProc)
  )

  def genkeyCmdProc(state: State): State = {
    val randkey = StrKey.random()
    println("Address:" + randkey.address)
    println("Seed:" + randkey)
    state
  }

}
