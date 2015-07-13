package io.scalac.akka.http.websockets

import java.net.URI

import akka.actor.{Actor, ActorSystem, Props}
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.{Draft, Draft_17}
import org.java_websocket.handshake.ServerHandshake

import scala.io.StdIn
import scala.util.Random

class WSClient(val config: WSConfig, actorSystem: ActorSystem, name: String) extends WebSocketClient(config.uri, config.draft) {


  override def onMessage(message: String): Unit = println(message)

  override def onError(ex: Exception): Unit = println("Websocket Error: " + ex.getMessage)

  override def onClose(code: Int, reason: String, remote: Boolean): Unit = println("Websocket closed")

  override def onOpen(handshakedata: ServerHandshake): Unit = println("Websocket opened for name=" + name)

  def spam(numberOfTimes: Int, message: String) = {
    val spamer = actorSystem.actorOf(Props(new Actor {

      import actorSystem.dispatcher

      import scala.concurrent.duration._

      var counter: Int = 0

      override def receive: Receive = {
        case message: String =>
          counter = counter + 1
          send(s"[$name ][$counter] message")
          if (counter < numberOfTimes)
            actorSystem.scheduler.scheduleOnce(rand.seconds, self, message)
      }


      def rand: Int = 1 + Random.nextInt(9) //message every 1-10 seconds
    }))
    spamer ! message
  }

}

object WSClient {
  def apply(name: String, channel: Int = 1000, numberOfMessages: Int = 1000000)(implicit actorSystem: ActorSystem, config: WSConfig): WSClient = {
    new WSClient(config, actorSystem, name)
  }
}

/**
 *
 * @param host host for connection
 * @param port port to connect to
 * @param draft // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
 */
case class WSConfig(host: String, port: Int, path: String, draft: Draft) {
  def uri: URI = new URI(s"ws://$host:$port/$path")
}

object SampleWSClientRunner extends App {

  implicit val defaultWSConfig = WSConfig("localhost", 8080, "ws-echo", new Draft_17())
  implicit val actorSystem = ActorSystem("websocket-client")

  val clientNames: List[String] = List("User X", "User Z", "User Y")

  val c: WSClient = WSClient("mario", 1294)

  c.connect()
  Thread.sleep(3000)

  c.spam(3, "hello message")

  println(s"Press RETURN to stop...")
  StdIn.readLine()

}