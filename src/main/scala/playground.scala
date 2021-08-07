import akka.actor.{Actor, ActorSystem, Props}

//This is how a basic Akka Actor and Actor system works, and how messages are passed to it
object playground extends App {

  class ExampleActor extends Actor {
    def receive: Receive = {
      case s: String => println("Received String: " + s)
      case i: Int => println("Received Number: " + i)
    }
  }

    val system = ActorSystem("ExampleSystem")
    val actor = system.actorOf(Props[ExampleActor],"ExampleActor1")

    actor!"Hi"
    actor!123
    actor!'c'

  system.terminate()
}
