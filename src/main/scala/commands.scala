/**
 * Created by kring on 2015/7/29.
 */
import sbt.Keys._
import sbt._

import org.strllar.stellarbase._

object StellarCommands extends AutoPlugin {
  override lazy val projectSettings = Seq(commands += Command.command("solarEscape")(solarEscapeCmdProc))
  override def trigger = allRequirements

  object Keys {
    val currNetwork = SettingKey[String]("network", "current network (XLM KLM)")
    val currRole = SettingKey[String]("role", "current role (Watcher Validator)")
    val currRunLevel = SettingKey[Int]("runlevel", "current run level")
    val currKeychain = SettingKey[List[StrSeed]]("stellar-securekey", "the secret keys")
    //val democmd = inputKey[String]("Demo")
  }

  class DynamicState(s :State) {
    //State contains mutable variables
    //Extracted contains Immutable variables from project definitions(in *.sbt or project/*.scala files)
    val states = collection.mutable.ListBuffer(s)

    def getByKey[T](k :SettingKey[T]): Option[T] = {
      states.head.get(k.key)
    }
    def setByKey[T](k :SettingKey[T], v :T): Unit = {
      states.prepend(states.head.put(k.key, v))
    }
    def updateByKey[T](k :SettingKey[T])(p :Option[T] => T) = {
      states.prepend(states.head.update(k.key)(p))
    }

    def state(reinit :Boolean =false) = if (reinit) {
      val extracted: Extracted = Project.extract(s)
      extracted.append(Seq(
        shellPrompt := ((st: State) => {
          st.get(Keys.currKeychain.key).flatMap(_.headOption).map(_.address.toString.take(6) ++ "..").getOrElse("_") + ">"
        })
      ), states.head).copy(
        definedCommands =
          supportedCommands.toNative ++
            Seq(BuiltinCommands.shell, BasicCommands.exit), //++BuiltinCommands.ConsoleCommands
        remainingCommands =
             (Exec("init", None) +: s.remainingCommands :+ Exec("shell", None))
      )
    }
    else {
      states.head
    }
  }

  val helpMessage =
    s"""
      |                                                             ---]===>
      |                       Welcome back to the Stellar Network!
      |Available command:
      |${supportedCommands.procs.map(cmd => cmd._1).mkString(", ")}
      |
    """.stripMargin

  def solarEscapeCmdProc(state: State): State = {
    println(helpMessage)
    val ds = new DynamicState(state)
    ds.state(true)
  }

  type CmdProc = (DynamicState) => Boolean
  def wrapProc(cmdproc: CmdProc) :(State) => State = {
    def cmd(state :State): State = {
      val ds = new DynamicState(state)
      cmdproc(ds)
      ds.state(false)
    }
    cmd
  }

  import complete.DefaultParsers._
  val digit = charClass(_.isDigit, "digit").examples("0", "1", "2")

  class CommandDefs(val procs :(String, CmdProc)*)(natives :Command*) {
    def toNative = {
      procs.map(x =>  Command.command(x._1)(wrapProc(x._2))) ++
        natives.toSeq
    }
  }

  lazy val supportedCommands = new CommandDefs(
    ("init", initCmd),
    ("newkey", genkeyCmd),
    ("who", listkeyCmd)
  )(
    Command("test")(s => digit)(testCmdProc)
  )

  def genkeyCmd = { s :DynamicState =>
    val randkey = StrKey.random()(Networks.XLMLive)
    println("Address:" + randkey.address)
    println("Seed:" + randkey)
    s.updateByKey(Keys.currKeychain)(randkey +: _.toList.flatMap(identity))
    true
  }

  def initCmd = { s :DynamicState =>
    true
  }


  def listkeyCmd = { s :DynamicState =>
    s.getByKey(Keys.currKeychain).toList.flatMap(identity).zipWithIndex.foreach{ x =>
      val (key, ord) = x
      println("[%d] %s :%s".format(ord, key.address.toString(), key.toString().toLowerCase()))
    }
    true
  }

  def testCmdProc(state: State, args:Char): State = {
    args.toString.toInt match {
      case 1 => {
        println("Start")
        //StellarCore.startInstance()
      }
      case 2 => {
        println("Stop")
        //StellarCore.stopInstace()
      }
      case 3 => {
        println("start http only")
        HttpServer.startServer("dist/")
      }
      case 4 => {
        //import StellarCore._
      }
    }
    state
  }
}
