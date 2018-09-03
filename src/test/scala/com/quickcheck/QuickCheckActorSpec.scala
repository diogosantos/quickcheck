package com.quickcheck

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import com.quickcheck.messages.SaveCheckRequest
import org.scalatest.{FunSpecLike, Matchers}

class QuickCheckActorSpec extends FunSpecLike with Matchers {

  implicit val system: ActorSystem = ActorSystem()

  describe("quick-check") {

    describe("given a single SaveCheckRequest message") {
      it("should store the message") {
        val message = "This is a single message"

        tellMessages(SaveCheckRequest(message)) { quickCheckActor =>

          quickCheckActor.lastMessage should equal(message)
        }
      }
    }

    describe("given multiple SaveCheckRequest messages") {
      it("should store only the last message") {
        val message2 = "This is the last message"

        tellMessages(SaveCheckRequest("This is the first message"), SaveCheckRequest(message2)) { quickCheckActor =>

          quickCheckActor.lastMessage should equal(message2)
        }
      }
    }

  }

  private def tellMessages(message: SaveCheckRequest*)(f: QuickCheckActor => Unit): Unit = {
    val actorRef = TestActorRef(new QuickCheckActor)
    message.foreach(actorRef ! _)
    f(actorRef.underlyingActor)
  }

}
