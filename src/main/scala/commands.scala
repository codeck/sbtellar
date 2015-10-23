/**
 * Created by kring on 2015/7/29.
 */
import sbt.Keys._
import sbt._

import org.strllar.stellarbase._

object StellarCommands extends AutoPlugin {
  override lazy val projectSettings = Seq(commands += Command.command("solarEscape")(solarEscapeCmdProc))
  override def trigger = allRequirements

  val currSecurekey = SettingKey[String]("stellar-securekey", "the secret key of current account")

  val demo = inputKey[String]("Demo")

  val helpMessage =
    """
      |                                                             ---]===>
      |                       Welcome the Stellar Network!
      |Available command:
      |  newkey, id, exit
      |
    """.stripMargin

  def solarEscapeCmdProc(state: State): State = {
    println(helpMessage)

    val extracted: Extracted = Project.extract(state)
    extracted.append(
      shellPrompt := (st => {
        Project.extract(st).getOpt(currSecurekey).flatMap(StrSeed.parse(_).toOption).map(_.address.toString).getOrElse("_") + ">"
      }), state).copy(
      definedCommands =
        supportedCommands ++
        Seq(BasicCommands.shell, BasicCommands.exit), //++BuiltinCommands.ConsoleCommands
      remainingCommands =
        "init" +: state.remainingCommands :+ "shell"
    )
  }

  import complete.DefaultParsers._
  val digit = charClass(_.isDigit, "digit").examples("0", "1", "2")

  lazy val supportedCommands = Seq(
    Command.command("newkey")(genkeyCmdProc),
    Command.command("id")(showkeyCmdProc),
    Command.command("init")(initCmdProc),
    Command("test")(x => digit)(testCmdProc)
  )

  def genkeyCmdProc(state: State): State = {
    val randkey = StrKey.random()
    println("Address:" + randkey.address)
    println("Seed:" + randkey)

    val extracted: Extracted = Project.extract(state)

    extracted.append(Seq(shellPrompt := (st => {
      Project.extract(st).getOpt(currSecurekey).flatMap(StrSeed.parse(_).toOption).map(_.address.toString.take(6)).getOrElse("") + "...>"
      }),currSecurekey := randkey.toString), state)
  }

  def initCmdProc(state: State): State = {

    println("inited")
    state
  }

  def showkeyCmdProc(state: State): State = {
    val extracted: Extracted = Project.extract(state)
    println(extracted.getOpt(currSecurekey).getOrElse("secure key not generated"))
    state
  }

  def testCmdProc(state: State, args:Char): State = {
    args.toString.toInt match {
      case 1 => {
        println("Start")
        StellarCore.startInstance()
      }
      case 2 => {
        println("Stop")
        StellarCore.stopInstace()
      }
      case 3 => {
        println("start http only")
        HttpServer.startServer("dist/")
      }
      case 4 => {
        import StellarCore._
      }
    }
    state
  }
}
