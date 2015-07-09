package io.scalac.akka.http.websockets.services

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.scalac.akka.http.websockets.services.WebService

object MainService extends WebService {

  override def route: Route = pathEndOrSingleSlash {
    complete("Welcome to websocket server")
  }

}
