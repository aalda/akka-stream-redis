package io.github.aalda.akka.stream.redis.scaladsl

import akka.NotUsed
import akka.stream.scaladsl.Sink
import akka.util.ByteString
import io.github.aalda.akka.stream.redis.{OutgoingMessage, RedisSinkSettings, RedisSinkStage}
import redis.ByteStringSerializer

object RedisSink {

  import ByteStringSerializer._

  /**
    * Scala API: Creates an [[RedisSink]] that accepts ByteString elements.
    */
  def simple(settings: RedisSinkSettings): Sink[OutgoingMessage[ByteString], NotUsed] = apply(settings)

  /**
    * Scala API: Creates an [[RedisSink]] that accepts [[OutgoingMessage]] elements.
    */
  def apply[V: ByteStringSerializer](settings: RedisSinkSettings): Sink[OutgoingMessage[V], NotUsed] =
    Sink.fromGraph(new RedisSinkStage(settings))

}
