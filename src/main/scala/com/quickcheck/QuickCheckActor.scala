package com.quickcheck

import akka.actor.Actor
import akka.event.Logging
import com.quickcheck.messages.SaveCheckRequest

class QuickCheckActor extends Actor {

  var lastMessage: String = ""

  val logger = Logging(context.system, this)

  override def receive = {
    case SaveCheckRequest(m) => {
      lastMessage = m
      logger.info("Last message received was {}", m)
    }
    case o => logger.info("Unknown message received: {}", o)
  }


}
