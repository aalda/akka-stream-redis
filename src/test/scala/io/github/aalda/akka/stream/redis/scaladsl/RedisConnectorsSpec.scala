/*
 * Copyright 2016 Alvaro Alda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.aalda.akka.stream.redis.scaladsl

import akka.stream.scaladsl.{ Sink, Source }
import akka.util.ByteString
import io.github.aalda.akka.stream.redis._

import scala.concurrent.duration._
import scala.language.postfixOps

class RedisConnectorsSpec extends RedisSpec {

  override implicit val patienceConfig = PatienceConfig(20 seconds)

  "The Redis connectors" should {

    "publish and consume elements through a simple channel" in {

      val redisSink = RedisSink.simple(
        DefaultRedisSinkSettings(RedisConnectionUri("redis://localhost:6379"))
      )

      val redisSource = RedisSource(
        DefaultRedisSourceSettings(RedisConnectionUri("redis://localhost:6379"), Vector("test")),
        bufferSize = 1
      )

      val input = Vector("one", "two", "three", "four", "five")

      // run sink
      Source(input).map(msg => OutgoingMessage("test", ByteString(msg))).runWith(redisSink)

      // run source
      val result = redisSource.map(_.data).take(input.size).runWith(Sink.seq)

      result.futureValue.map(_.utf8String) shouldEqual input

    }

  }

}
