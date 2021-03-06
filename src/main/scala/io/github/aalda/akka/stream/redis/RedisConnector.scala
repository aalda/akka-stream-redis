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

package io.github.aalda.akka.stream.redis

import java.net.URI

import akka.actor.ActorSystem
import redis.api.pubsub.{ Message, PMessage }
import redis.{ RedisClient, RedisPubSub }

import scala.language.postfixOps

/**
 * Internal API
 */
private[redis] trait RedisConnector {

  def redisClientFrom(settings: RedisConnectionSettings)(implicit system: ActorSystem): RedisClient =
    settings match {
      case RedisConnectionUri(uri) =>
        val uriObj = new URI(uri)
        RedisClient(uriObj.getHost, uriObj.getPort)
      case RedisConnectionDetails(host, port, authPass) =>
        RedisClient(host, port, authPass)
    }

  def redisPubSubFrom(settings: RedisSourceSettings, onMessage: Message => Unit, onPMessage: PMessage => Unit)(
      implicit system: ActorSystem): RedisPubSub =
    settings.connectionSettings match {
      case RedisConnectionUri(uri) =>
        val uriObj = new URI(uri)
        RedisPubSub(uriObj.getHost, uriObj.getPort, settings.channels, settings.patterns, onMessage, onPMessage)
      case RedisConnectionDetails(host, port, authPass) =>
        RedisPubSub(host, port, settings.channels, settings.patterns, onMessage, onPMessage, authPass)
    }

}
// TODO complete with RedisPool and SentinelClient
