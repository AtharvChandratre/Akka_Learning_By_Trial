import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object Countdown extends App {

  case class StartCountDown (n:Int, otherActor: ActorRef )
  case class CountDown (n:Int)

  class CountdownActor extends Actor {
    def receive: Receive = {
      case StartCountDown(n,otherActor) =>
        println(n)
        otherActor!CountDown(n-1)
      case CountDown(n) =>
        println(self)
        if(n>0){
          println(n)
          sender!CountDown(n-1)
        }
        else {
          context.system.terminate()
        }
    }
  }

  val system = ActorSystem("CountDownSystem")
  val actor1 = system.actorOf(Props[CountdownActor],"CountDownActor1")
  val actor2 = system.actorOf(Props[CountdownActor],"CountDownActor2")

  actor1!StartCountDown(10,actor2)
}
