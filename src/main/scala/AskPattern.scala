import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

//This is how the Akka Ask Pattern works
//It's similar to the way we are used to running functions, giving some input and expecting some output
object AskPattern extends App {
  case object AskName
  case class NameResponse(name:String)
  case class askNameOther(other:ActorRef)

  class AskPatternActor (val name:String) extends Actor {
    def receive:Receive = {
      case AskName =>
//        Thread.sleep(10000)
        sender!NameResponse(name)
      case askNameOther(other) =>
        val future = other?AskName
        future.onComplete{
          case Success(nr: NameResponse) =>
            println("Name is "+nr.name)
          case Success(s) =>
            println("Successful but no name received")
          case Failure(ex) =>
            println("Failed")
        }

    }
  }

  val system = ActorSystem("AskPatternSystem")
  val actor1 = system.actorOf(Props(new AskPatternActor("Name1")),"AskActor1")
  val actor2 = system.actorOf(Props(new AskPatternActor("Name2")),"AskActor2")

  implicit val timeout: Timeout = Timeout(1.second)
  val answer = actor1?AskName
  answer.foreach(println)

  actor1!askNameOther(actor2)

  system.terminate()
}
