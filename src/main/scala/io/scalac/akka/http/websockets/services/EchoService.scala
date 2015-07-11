package io.scalac.akka.http.websockets.services

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Flow
import io.scalac.akka.http.websockets.services.WebService

object EchoService extends WebService {

  override def route: Route = path("ws-echo") {
    get {
      handleWebsocketMessages(echoService)
    }
  }

  val echoService: Flow[Message, Message, _] = Flow[Message].map {
    case TextMessage.Strict(txt) => TextMessage("ECHO: " + txt)
    case _ => TextMessage("Message type unsupported")
  }
}
