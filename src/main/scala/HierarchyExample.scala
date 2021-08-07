import akka.actor.{Actor, ActorSystem, Props}

//This experiment explains how an actor can be used to spawn child actors
// It also shows how context can be used to iterate through an actor's children
object HierarchyExample extends App{

  case object CreateChildActor
  case class SignalChildren (id: Int)
  case class PrintSignal (id: Int)

  class ParentActor extends Actor {
    private var n = 1
    def receive:Receive = {
      case CreateChildActor =>
        context.actorOf(Props[ChildActor],"Child"+n)
        n+=1
      case SignalChildren(id) =>
        context.children.foreach(_!PrintSignal(id))
    }
  }

  class ChildActor extends Actor {
    def receive:Receive = {
      case PrintSignal(id) =>
        println(id+" "+self)
    }
  }

  val system = ActorSystem("HierarchySystem")
  val actor = system.actorOf(Props[ParentActor],"ParentActor")

  actor!CreateChildActor
  actor!SignalChildren(1)
  actor!CreateChildActor
  actor!CreateChildActor
  actor!SignalChildren(2)

//  Print Signal of child 1 only by grabbing it using the URL
//  Note - This does not have to be in order
  val child1 = system.actorSelection("akka://HierarchySystem/user/ParentActor/Child1")
  child1!PrintSignal(3)

  system.terminate()
}
