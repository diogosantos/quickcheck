package com.quickcheck

import akka.actor.{Actor, ActorSystem, Props, Status}
import akka.event.Logging
import com.quickcheck.messages.{InvalidReversibleStringException, ReverseStringRequest, SaveCheckRequest}

class QuickCheckActor extends Actor {

  val logger = Logging(context.system, this)
  var lastMessage: String = ""

  override def receive = {
    case SaveCheckRequest(m) => {
      logger.info("Last message received was {}", m)
      lastMessage = m
      sender() ! Status.Success()
    }
    case ReverseStringRequest(m) if m.isEmpty => {
      logger.info("Received an invalid reversible string: {}", m)
      sender() ! Status.Failure(InvalidReversibleStringException(m))
    }
    case ReverseStringRequest(m) => {
      logger.info("Received a ReverseStringRequest: {}", m)
      sender() ! Status.Success(m.reverse)
    }
    case o => {
      logger.info("Unknown message received: {}", o)
      sender() ! Status.Failure(new ClassNotFoundException())
    }
  }

}

object Main extends App {
  private val system = ActorSystem("quick-check")
  system.actorOf(Props[QuickCheckActor], name = "quick-check-server")
}