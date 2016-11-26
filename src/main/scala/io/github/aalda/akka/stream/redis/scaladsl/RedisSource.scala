package io.github.aalda.akka.stream.redis.scaladsl

import akka.NotUsed
import akka.stream.scaladsl.Source
import io.github.aalda.akka.stream.redis.{IncomingMessage, RedisSourceSettings, RedisSourceStage}

object RedisSource {

  /**
    * Scala API: Creates an [[RedisSource]] with given settings and buffer size.
    */
  def apply(settings: RedisSourceSettings, bufferSize: Int): Source[IncomingMessage, NotUsed] =
    Source.fromGraph(new RedisSourceStage(settings, bufferSize))

}
