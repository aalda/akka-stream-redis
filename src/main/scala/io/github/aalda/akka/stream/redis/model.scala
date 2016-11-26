package io.github.aalda.akka.stream.redis

/**
  * Internal API
  */
sealed trait RedisConnectorSettings {
  def connectionSettings: RedisConnectionSettings
}

sealed trait RedisSourceSettings extends RedisConnectorSettings {
  def channels: Seq[String]
  def patterns: Seq[String]
}

final case class DefaultRedisSourceSettings(connectionSettings: RedisConnectionSettings,
                                            channels: Seq[String] = Seq.empty,
                                            patterns: Seq[String] = Seq.empty) extends RedisSourceSettings {

  @annotation.varargs
  def withChannels(channels: String*) = copy(channels = channels.toList)

  @annotation.varargs
  def withPatterns(patterns: String*) = copy(patterns = patterns.toList)

}

sealed trait RedisSinkSettings extends RedisConnectorSettings {
  def channel: String
}

final case class DefaultRedisSinkSettings(connectionSettings: RedisConnectionSettings,
                                          channel: String) extends RedisSinkSettings

/**
  * Only for internal implementations
  */
sealed trait RedisConnectionSettings

final case class RedisConnectionUri(uri: String) extends RedisConnectionSettings

// TODO complete
final case class RedisConnectionDetails(host: String,
                                        port: Int,
                                        authPassword: Option[String] = None
                                       ) extends RedisConnectionSettings