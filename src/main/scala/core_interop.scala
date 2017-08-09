/**
 * Created by kring on 2015/8/25.
 */

import java.io.{FileOutputStream, ByteArrayInputStream, File}
import java.nio.file.Paths
import java.text.DateFormat
import java.util.{TimeZone, Calendar}

import akka.util.{ByteString, ByteStringBuilder}
import org.strllar.stellarbase._
/*
import org.strllar.stellarbase.compiled_xdr.Operation.OperationBody
import org.strllar.stellarbase.compiled_xdr.Transaction.TransactionExt
import org.strllar.stellarbase.compiled_xdr._

import scala.sys.process.{ProcessLogger, Process}

object StellarCore {

  trait NetworkSetting {
    val safeNet: Boolean
    val maxFailure: Int
    val quorumSet: QuorumSet
  }

  case class StandaloneSetting(validseed: StrSeed)
  object TestnetSetting extends NetworkSetting{
    val node1 = StrAddress.parse("GDKXE2OZMJIPOSLNA6N6F2BVCI3O777I2OOC4BV7VOYUEHYX7RTRYA7Y").get
    val node2 = StrAddress.parse("GCUCJTIYXSOXKBSNFGNFWW5MUQ54HKRPGJUTQFJ5RQXZXNOLNXYDHRAP").get
    val node3 = StrAddress.parse("GC2V2EFSXN6SQTWVYA5EPJPBWWIMSD2XQNKUOHGEKB535AQE2I6IXV2Z").get

    override val safeNet = false;
    override val maxFailure = 0;
    override val quorumSet = QuorumSet(node1, node2, node3)
  }

  type RunMode = Either[StandaloneSetting, NetworkSetting]

  def newHistCmd(name: String = "local") = Seq(Paths.get("bin", "stellar-core").toString, "--conf", "-", "--newhist", name)

  def newDBandForceSCPCmd = Seq(Paths.get("bin", "stellar-core").toString, "--conf", "-", "--newdb", "--forcescp")

  def startCmd = Seq(Paths.get("bin", "stellar-core").toString, "--conf", "-")

  val distDir = "dist"

  def runStellarCore(args:String*)(config :String) {
    val cmd = Seq(Paths.get("bin", "stellar-core").toString, "--conf", "-") ++ args
    val coreLogger = ProcessLogger(
      outline => { println("|" + outline) },
      errline => { println("-" + errline) })
    val config = generateConfig(Left(StandaloneSetting(StrKey.random())))
    Process(cmd, new File(distDir)).#<(new ByteArrayInputStream(config.getBytes)).!(coreLogger)
  }

  def runStellarCoreAsync(args:String*)(config :String) = {
    val cmd = Seq(Paths.get("bin", "stellar-core").toString, "--conf", "-") ++ args
    val coreLogger = ProcessLogger(
      outline => { println("|" + outline) },
      errline => { println("-" + errline) })
    val config = generateConfig(Left(StandaloneSetting(StrKey.random())))
    Process(cmd, new File(distDir)).#<(new ByteArrayInputStream(config.getBytes)).run(coreLogger)
  }

  def generateConfig(mode: RunMode): String = {
    import stelalr_core_cfg_tpl._
    generalConfig ++
      (mode match {
        case Left(ss) => {
          setStandAlone ++ secureSetting(false, 0) ++ validatorSeed(ss.validseed.toString) ++ QuorumSet.toConfigString(QuorumSet(ss.validseed.address)) ++ localHistory()
        }
        case Right(ns) => {
          secureSetting(ns.safeNet, ns.maxFailure) ++ QuorumSet.toConfigString(ns.quorumSet)
        }
      })
  }

  def freeBackupDir(prefix :String) = Iterator.from(1).map(x => new File(s"$prefix.$x")).filter(_.exists() == false).next()

  def vacuumDir(distdir :String): Unit = {
    val dir = new File(distdir)
    if (dir.exists()) {
      val newname = freeBackupDir(distdir)
      println("Backup to "+newname.getName)
      if (dir.renameTo(newname)) {
        println("Backup succeed")
      }
      else {
        println("Backup failed")
      }
    }
    dir.mkdir()
  }

  def newHistory(): Unit = {
    //runStellarCore( "--newhist", "local")("")
    val distdir = "dist"
    val config = generateConfig(Left(StandaloneSetting(StrKey.random())))
    val coreLogger = ProcessLogger(outline => {
      println("|" + outline)
    }, errline => {
      println("-" + errline)
    })
    Process(newHistCmd(), new File(distdir)).#<(new ByteArrayInputStream(config.getBytes)).!(coreLogger)
  }

  def newDBandForceSCP(): Unit = {
    val distdir = "dist"
    val config = generateConfig(Left(StandaloneSetting(StrKey.random())))
    val coreLogger = ProcessLogger(outline => {
      println("**" + outline)
    }, errline => {
      println("!!" + errline)
    })
    Process(newDBandForceSCPCmd, new File(distdir)).#<(new ByteArrayInputStream(config.getBytes)).!(coreLogger)
  }

  var coreprocess :Option[Process] = None

  def startCore(): Unit = {
    val distdir = "dist"
    val config = generateConfig(Left(StandaloneSetting(StrKey.random())))
    val coreLogger = ProcessLogger(outline => {
      println("*" + outline)
    }, errline => {
      println("!" + errline)
    })
    coreprocess = Some(Process(startCmd, new File(distdir)).#<(new ByteArrayInputStream(config.getBytes)).run(coreLogger))
  }

  def startInstance(): Unit = {
    vacuumDir("dist")
    HttpServer.startServer("dist/")
    newHistory()
    newDBandForceSCP()
    startCore()
  }

  def stopInstace(): Unit = {
    StellarCore.coreprocess.foreach(_.destroy())
    HttpServer.stopServer();
  }

  def reflectAccount(addr: String) = {
    case class AccountInfo(addr: String, seqno: Long, balance: Long)
  }

  class AccountOP(val strAddress: StrAddress) {

    val uint256 = new Uint256
    uint256.setuint256(strAddress.byteBuffer)
    val pubkey = new PublicKey
    pubkey.setDiscriminant(CryptoKeyType.KEY_TYPE_ED25519)
    pubkey.seted25519(uint256)
    val acct = new AccountID
    acct.setAccountID(pubkey)

  }

  class AccountOwner(val strSeed: StrSeed) extends AccountOP(strSeed.address) {
    def eazyPay(destAddress: StrAddress) = {
      val op = new PaymentOp
      op.setdestination(new AccountOP(destAddress).acct)
      //...
    }
  }
}
*/