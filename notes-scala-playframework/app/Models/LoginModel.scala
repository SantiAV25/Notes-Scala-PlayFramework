package Models

object loginModelObj{
  import play.api.data.Forms._
  import play.api.data.Form

  case class LoginUsermodel(userName: String,password: String)

  val formLogin = Form(
    mapping(
      "userName" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginUsermodel.apply)(LoginUsermodel.unapply)
  )
}