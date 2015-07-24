package io.scalac.akka.http.websockets

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import io.scalac.akka.http.websockets.services.{ChatService, EchoService, MainService}

import scala.io.StdIn

object Server extends App {

  import Directives._

  implicit val actorSystem = ActorSystem("akka-system")
  implicit val flowMaterializer = ActorMaterializer()

  val config = actorSystem.settings.config
  val interface = config.getString("app.interface")

  val port = config.getInt("app.port")

  val route = MainService.route ~
    EchoService.route ~
    ChatService.route

  val binding = Http().bindAndHandle(route, interface, port)
  println(s"Server is now online at http://$interface:$port\nPress RETURN to stop...")

  /**
   * Run WSClient when `with-client` argument is provided
   */
  alternativelyRunTheClient()

  StdIn.readLine()

  import actorSystem.dispatcher

  binding.flatMap(_.unbind()).onComplete(_ => actorSystem.shutdown())
  println("Server is down...")

  private def alternativelyRunTheClient(): Unit = {

    if (args.head.equalsIgnoreCase("with-client")) {
      val c: WSClient = WSClient("http://localhost:8080/ws-chat/123?name=HAL1000", "HAL1000")

      if (c.connectBlocking())
        c.spam("hello message")
    }

  }

}
