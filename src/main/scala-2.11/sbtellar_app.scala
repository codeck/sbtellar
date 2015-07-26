/**
 * Created by kring on 2015/7/18.
 */

import java.io.{FileOutputStream, File}
import java.nio.file.{FileSystems, Path, Paths}

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, HttpRequest, Uri}
import akka.http.scaladsl.server.{RequestContext, RouteResult}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.{Http}
import akka.http.scaladsl.server.Directives._
import akka.stream.io.OutputStreamSink

import scala.sys.process._

object SbtellarApp extends App {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()

  val route =
    path("info") {
      (rc: RequestContext) => {
        Http().singleRequest(HttpRequest(uri = Uri("http://localhost:39132/info"))).
          flatMap { (resp) =>
          rc.complete(resp)
        }(rc.executionContext)
      }
    }~
      path("hist" / RestPath) { pathRest =>
        get {
          getFromFile(new File("history/" + pathRest))
        } ~
          put {
            extractLog { logger =>
            { (rc:RequestContext) =>
                val file = new File("history/" +pathRest)
                file.getParentFile().mkdirs();
                file.createNewFile();
                val mat = rc.request.entity.dataBytes.runWith(OutputStreamSink(()=>{new FileOutputStream(file)}))
                mat.flatMap( filesize => {
                  //logger.info(s"wrote $pathRest $filesize/${rc.request.entity.getContentLengthOption()} bytes")
                  rc.complete("OK")
                })(rc.executionContext)
            }
            }
          }
      } ~
      path("tx") {
        //support `curl -F blob="BASE64+/ENCODED+/TRANSACTION" http://localhost:8080/tx` without manual urlencoding
        formFields('blob) {
          (blob) =>  {
          (rc: RequestContext) => {
          Http().singleRequest(HttpRequest(uri = Uri("http://localhost:39132/tx").withQuery(("blob", blob)))).
            flatMap { (resp) =>
            rc.complete(resp)
          }(rc.executionContext)
          }
          }
        }
      }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  Seq(Paths.get("bin", "stellar-core").toString,"--conf", "bin/stellar-core.cfg", "--newhist", "single") !

  Seq(Paths.get("bin", "stellar-core").toString,"--conf", "bin/stellar-core.cfg", "--newdb", "--forcescp") !

  val core = Seq(Paths.get("bin", "stellar-core").toString,"--conf", "bin/stellar-core.cfg").run()

  scala.io.StdIn.readLine(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

  import system.dispatcher // for the future transformations
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => {
    core.destroy()
    system.shutdown()
  }) // and shutdown when done
}
