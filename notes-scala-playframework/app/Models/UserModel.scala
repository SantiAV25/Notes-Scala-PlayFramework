package Models

import Models.noteModel

case class userModel(
  userName: String,
  password: String,
  email: String,
  nombre: String,
  apellido: String,
  notes: List[noteModel]
)

object userModelObj{
  import play.api.data.Forms._
  import play.api.data.Form

  case class DataUsermodel(userName: String,password: String,email: String, nombre: String, apellido: String)

  val formRegister = Form(
    mapping(
      "userName" -> nonEmptyText,
      "password" -> nonEmptyText,
      "email" -> nonEmptyText,
      "nombre" -> nonEmptyText,
      "apellido" -> nonEmptyText
    )(DataUsermodel.apply)(DataUsermodel.unapply)
  )
}