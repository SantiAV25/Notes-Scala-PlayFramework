package controllers
import play.api.db.Databases

object  DataBaseController{
val database = Databases(
  driver = "org.postgresql.Driver",
  url = "jdbc:postgresql://localhost/ScalaNotes",
  name = "ScalaDataBase",
  config = Map(
    "username" -> "ScalaNotesApp",
    "password" -> "scala"
  )
)
}