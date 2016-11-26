package io.github.aalda.akka.stream.redis.scaladsl

import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString
import io.github.aalda.akka.stream.redis._
import redis.RedisClient

import scala.concurrent.duration._
import scala.language.postfixOps

class RedisConnectorsSpec extends RedisSpec {

  override implicit val patienceConfig = PatienceConfig(20 seconds)

  "The Redis connectors" should {

    "publish and consume elements through a simple channel" in {

      val redisSink = RedisSink.simple(
        DefaultRedisSinkSettings(RedisConnectionUri("redis://localhost:6379"), "test")
      )

      val redisSource = RedisSource(
        DefaultRedisSourceSettings(RedisConnectionUri("redis://localhost:6379"), Vector("test")),
        bufferSize = 10
      )

      val input = Vector("one", "two", "three", "four", "five")

      // run sink
      Source(input).map(msg => OutgoingMessage(ByteString(msg))).runWith(redisSink)

      // run source
      val result = redisSource.map(_.data).take(input.size).runWith(Sink.seq)

      result.futureValue.map(_.utf8String) shouldEqual input

    }

  }

}
