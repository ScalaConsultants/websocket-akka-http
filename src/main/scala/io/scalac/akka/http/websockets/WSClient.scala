package io.scalac.akka.http.websockets

import java.net.URI

import akka.actor.{Actor, ActorSystem, Props}
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_17
import org.java_websocket.handshake.ServerHandshake

import scala.io.StdIn
import scala.util.Random

class WSClient(url: String, name: String, actorSystem: ActorSystem) extends WebSocketClient(new URI(url), new Draft_17()) {

  override def onMessage(message: String): Unit = println(message)

  override def onError(ex: Exception): Unit = println("Websocket Error: " + ex.getMessage)

  override def onClose(code: Int, reason: String, remote: Boolean): Unit = println("Websocket closed")

  override def onOpen(handshakedata: ServerHandshake): Unit = println("Websocket opened for name=" + name)

  def spam(message: String, numberOfTimes: Int = 1000) = {
    val talkActor = actorSystem.actorOf(Props(new Actor {

      import actorSystem.dispatcher

      import scala.concurrent.duration._

      var counter: Int = 0

      override def receive: Receive = {
        case message: String =>
          counter = counter + 1
          send(s"[$name] message #$counter")
          if (counter < numberOfTimes)
            actorSystem.scheduler.scheduleOnce(rand.seconds, self, message)
      }

      def rand: Int = 1 + Random.nextInt(9) //message every 1-10 seconds
    }))
    talkActor ! message
  }

}

object WSClient {
  def apply(url: String, name: String)(implicit actorSystem: ActorSystem): WSClient = {
    new WSClient(url, name, actorSystem)
  }
}