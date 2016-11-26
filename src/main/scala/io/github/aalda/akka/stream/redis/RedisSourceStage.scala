package io.github.aalda.akka.stream.redis

import akka.actor.ActorSystem
import akka.stream.stage.{AsyncCallback, GraphStage, GraphStageLogic, OutHandler}
import akka.stream.{ActorMaterializer, Attributes, Outlet, SourceShape}
import akka.util.ByteString
import redis.RedisPubSub
import redis.api.pubsub.{Message, PMessage}

import scala.collection.mutable
import scala.concurrent.ExecutionContext

final case class IncomingMessage(channel: String, data: ByteString, patternMatched: Option[String] = None)

object RedisSourceStage {
  private val defaultAttributes = Attributes.name("RedisSource")
}

/**
  * Connects to an Redis Pub/Sub server upon materialization and consumes messages from it emitting them
  * into the stream. Each materialized source will create one connection to the server.
  * As soon as an `IncomingMessage` is sent downstream, an ack for it is sent to the server.
  *
  * @param bufferSize The max number of elements to prefetch and buffer at any given time.
  */

class RedisSourceStage(settings: RedisSourceSettings, bufferSize: Int)
  extends GraphStage[SourceShape[IncomingMessage]] with RedisConnector { stage =>

  val out = Outlet[IncomingMessage]("RedisSource.out")

  override val shape: SourceShape[IncomingMessage] = SourceShape.of(out)

  override protected def initialAttributes: Attributes = RedisSourceStage.defaultAttributes

  @scala.throws[Exception](classOf[Exception])
  def createLogic(inheritedAttributes: Attributes): GraphStageLogic = new GraphStageLogic(shape) {

    private val settings: RedisSourceSettings = stage.settings

    private implicit var system: ActorSystem = _

    private implicit var ec: ExecutionContext = _

    private val queue = mutable.Queue[IncomingMessage]()

    private var redisPubSub: RedisPubSub = _

    private var callback: AsyncCallback[IncomingMessage] = _

    override def preStart(): Unit = {
      system = materializer.asInstanceOf[ActorMaterializer].system
      ec = materializer.executionContext
      callback = getAsyncCallback[IncomingMessage]((msg) => handleIncomingMessage(msg))
      redisPubSub = redisPubSubFrom(settings, handleMessage, handlePMessage)
    }

    override def postStop(): Unit = {
      if (redisPubSub ne null) {
        redisPubSub.unsubscribe(settings.channels: _*)
        redisPubSub.punsubscribe(settings.patterns: _*)
        redisPubSub.stop()
        redisPubSub = null
      }
    }

    private def redisPubSubFrom(settings: RedisSourceSettings,
                                onMessage: Message => Unit,
                                onPMessage: PMessage => Unit)(implicit system: ActorSystem): RedisPubSub = {
      stage.redisPubSubFrom(settings, onMessage, onPMessage)
    }

    private def handleMessage(message: Message): Unit =
      callback.invoke(IncomingMessage(message.channel, message.data))

    private def handlePMessage(message: PMessage): Unit =
      callback.invoke(IncomingMessage(message.channel, message.data, Some(message.patternMatched)))

    private def handleIncomingMessage(message: IncomingMessage): Unit =
      if (isAvailable(out)) {
        push(out, message)
      } else {
        if (queue.size + 1 > bufferSize) {
          failStage(new RuntimeException(s"Reached maximum buffer size $bufferSize"))
        } else {
          queue.enqueue(message)
        }
      }

    setHandler(out, new OutHandler {
      @scala.throws[Exception](classOf[Exception])
      def onPull(): Unit = {
        if (queue.nonEmpty) {
          push(out, queue.dequeue())
        }
      }
    })

  }
}

