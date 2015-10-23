import java.io.{FileOutputStream, File}
import java.net.SocketOptions

import akka.actor.ActorSystem
import akka.http.ServerSettings
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.{Uri, HttpRequest}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RequestContext
import akka.http.scaladsl.server.directives.FormFieldDirectives
import akka.io.Inet
import akka.stream.ActorMaterializer
import akka.stream.io.OutputStreamSink
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future

object HttpServer {

  //workaround for akka in sbt
  //implicit val system = ActorSystem("server-system")
  val cl = getClass.getClassLoader
  implicit val system = ActorSystem("server-system", ConfigFactory.load(cl), cl)

  implicit val materializer = ActorMaterializer()

  var maybeBindingFuture:Option[Future[ServerBinding]] = None

  def startServer(distroot :String) {
    val route =
      path("hist" / RestPath) { pathRest =>
        get {
          getFromFile(new File(distroot + "history/" + pathRest))
        } ~
          put {
            extractLog { logger => { (rc: RequestContext) =>
              val file = new File(distroot + "history/" + pathRest)
              file.getParentFile().mkdirs();
              file.createNewFile();
              val mat = rc.request.entity.dataBytes.runWith(OutputStreamSink(() => {
                new FileOutputStream(file)
              }))
              mat.flatMap(filesize => {
                //logger.info(s"wrote $pathRest $filesize/${rc.request.entity.getContentLengthOption()} bytes")
                rc.complete("OK")
              })(rc.executionContext)
            }
            }
          }
      } ~
        path("tx") {
          //support `curl -F blob="BASE64+/ENCODED+/TRANSACTION" http://localhost:8080/tx` without manual urlencoding
          formFields(FormFieldDirectives.symbol2NR('blob)) {
            (blob) =>  {
              (rc: RequestContext) => {
                Http().singleRequest(HttpRequest(uri = Uri("http://localhost:11626/tx").withQuery(("blob", blob)))).
                  flatMap { (resp) =>
                  rc.complete(resp)
                }(rc.executionContext)
              }
            }
          }
        }
    maybeBindingFuture = Some(Http().bindAndHandle(route, "localhost", 8080,
      ServerSettings(system).copy(socketOptions = scala.collection.immutable.Seq(Inet.SO.ReuseAddress(true)))))
  }
  def stopServer() {
    import system.dispatcher
    // for the future transformations
    maybeBindingFuture.map(bindingFuture =>
      bindingFuture.flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => {
        println("unbonded")
      //system.shutdown()
    }) // and shutdown when done
    )
  }
}
