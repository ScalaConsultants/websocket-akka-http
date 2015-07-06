package io.scalac.akka.http.websockets

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.io.StdIn

object Server extends App {

  implicit val actorSystem = ActorSystem("akka-system")

  implicit val flowMaterializer = ActorMaterializer()

  val config = actorSystem.settings.config
  val interface = config.getString("app.interface")
  val port = config.getInt("app.port")

  val binding = Http().bindAndHandle(route, interface, port)

  println(s"Server is now online at http://$interface:$port\nPress RETURN to stop.")
  StdIn.readLine()

  import actorSystem.dispatcher

  binding.flatMap(_.unbind()).onComplete(_ => actorSystem.shutdown())

  def route =
    get {
      complete("Hello message")
    }
}
