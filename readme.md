##Websocket server

It's sample code that shows how to use `akka-http` and `akka-streams` to build custom websocket server.

You can read more about the code [here](http://blog.scalac.io/2015/07/30/websocket-server-with-akka-http.html)

to run server:

    sbt run
    
and you can connect to any chat room: http://localhost:8080/ws-chat/ROOM_NUMBER?name=YOUR_NAME

if you want to backgound actor that spam messages to channel number 123

    sbt "run with-client"
    
    