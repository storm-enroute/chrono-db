package org.chronodb



import scala.util.Try



trait Driver {
  def create(target: String, props: (String, Any)*): Try[Unit]
  def insert(target: String, timestamp: Long, value: Double): Try[Unit]
  def render(target: String, from: Long, until: Long, format: String): Try[String] = {
    ???
  }
}
