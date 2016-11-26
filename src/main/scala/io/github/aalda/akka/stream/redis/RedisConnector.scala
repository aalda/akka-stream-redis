package io.github.aalda.akka.stream.redis

import java.net.URI

import akka.actor.ActorSystem
import akka.stream.stage.GraphStageLogic
import redis.api.pubsub.{Message, PMessage}
import redis.{RedisClient, RedisPubSub}

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

  def redisPubSubFrom(settings: RedisSourceSettings,
                      onMessage: Message => Unit,
                      onPMessage: PMessage => Unit)(implicit system: ActorSystem): RedisPubSub = {
    settings.connectionSettings match {
      case RedisConnectionUri(uri) =>
        val uriObj = new URI(uri)
        RedisPubSub(uriObj.getHost, uriObj.getPort, settings.channels, settings.patterns, onMessage, onPMessage)
      case RedisConnectionDetails(host, port, authPass) =>
        RedisPubSub(host, port, settings.channels, settings.patterns, onMessage, onPMessage, authPass)
    }
  }

}
// TODO complete with RedisPool and SentinelClient