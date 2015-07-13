package io.scalac.akka.http.websockets.chat

import akka.actor.ActorSystem

object ChatRooms {
  var chatRooms: Map[Int, ChatRoom] = Map.empty[Int, ChatRoom]

  def findOrCreate(number: Int)(implicit actorSystem: ActorSystem): ChatRoom = chatRooms.getOrElse(number, createNewChatRoom(number))

  private def createNewChatRoom(number: Int)(implicit actorSystem: ActorSystem): ChatRoom = {
    val chatroom = ChatRoom(number)
    chatRooms += number -> chatroom
    chatroom
  }

}
