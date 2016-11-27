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

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpec }
import org.scalatest.concurrent.ScalaFutures

abstract class RedisSpec extends WordSpec with Matchers with BeforeAndAfterAll with ScalaFutures {

  implicit val system = ActorSystem(this.getClass.getSimpleName)
  implicit val materializer = ActorMaterializer()

  override protected def afterAll(): Unit = system.terminate()

}
