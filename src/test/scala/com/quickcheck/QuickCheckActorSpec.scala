package com.quickcheck

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.Timeout
import com.quickcheck.messages.{InvalidReversibleStringException, QCRequest, ReverseStringRequest, SaveCheckRequest}
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


class QuickCheckActorSpec extends FunSpecLike with Matchers {

  import scala.concurrent.ExecutionContext.Implicits.global

  private implicit val system: ActorSystem = ActorSystem()
  private implicit val timeout: Timeout = Timeout(2 seconds)

  private implicit val quickCheckActor: ActorRef = system.actorOf(Props(classOf[QuickCheckActor]))

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

    describe("given a ReverseStringRequest message") {

      it("should reverse the string") {
        val reverseFuture = askReverse("This should be reversed")

        val result = Await.result(reverseFuture.mapTo[String], 1 seconds)
        result should be("This should be reversed".reverse)
      }

      it("should fail on invalid string") {
        val failureFuture = askReverse("")

        intercept[InvalidReversibleStringException] {
          Await.result(failureFuture, 1 second)
        }
      }
    }

    describe("given multiple ReverseStringRequest message") {

      it("should reverse all the strings") {
        val fruits = Seq("pineapple", "", "apple", "orange")

        val responses: Seq[Future[String]] = fruits.map(x => askReverse(x))

        val listOfFutures = Future.sequence(responses.map(f => f.recover { case _:InvalidReversibleStringException => "" }))
        val results = Await.result(listOfFutures, 1 second)

        val expected = fruits.map(_.reverse)
        results should equal(expected)
      }

    }

  }

  private def askReverse(m: String): Future[String] = {
    (quickCheckActor ? ReverseStringRequest(m)).mapTo[String]
  }

  private def tellMessages[A <: QCRequest](message: A*)(f: QuickCheckActor => Unit): Unit = {
    val actorRef = TestActorRef(new QuickCheckActor)
    message.foreach(actorRef ! _)
    f(actorRef.underlyingActor)
  }

}
