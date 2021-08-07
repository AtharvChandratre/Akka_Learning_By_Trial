import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

//This is how the Akka Ask Pattern works
//It's similar to the way we are used to running functions, giving some input and expecting some output
object AskPattern extends App {
  case object askName
  case class NameResponse(name:String)

  class AskPatternActor (val name:String) extends Actor {
    def receive:Receive = {
      case askName =>
//        Thread.sleep(10000)
        sender!NameResponse(name)
    }
  }

  val system = ActorSystem("AskPatternSystem")
  val actor = system.actorOf(Props(new AskPatternActor("Hi, I'm Ask Actor!")),"AskActor")

  implicit val timeout: Timeout = Timeout(1.second)
  val answer = actor?askName
  answer.foreach(println)

  system.terminate()
}
