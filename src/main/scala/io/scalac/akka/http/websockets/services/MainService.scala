package io.scalac.akka.http.websockets.services

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object MainService {

  def route: Route = pathEndOrSingleSlash {
    complete("Welcome to websocket server")
  }

}
