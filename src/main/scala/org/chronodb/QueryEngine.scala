package org.chronodb



import scala.util.Failure
import scala.util.Success
import scala.util.Try



trait QueryEngine


object QueryEngine {
  val tableNameRegex = "^([A-Za-z_][A-Za-z0-9_]*)(\\.[A-Za-z_][A-Za-z0-9_]*)*$".r
  def validateTableName(name: String): Try[Unit] = {
    name match {
      case tableNameRegex(_, _) => Success(())
      case _ => Failure(new Exception(s"Invalid table name '$name'."))
    }
  }
}
