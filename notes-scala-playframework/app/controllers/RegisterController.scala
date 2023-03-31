package controllers

import javax.inject.Inject

import play.api.data._
import play.api.i18n._
import play.api.mvc._

import java.sql.Connection
import play.api.db.Database
import java.sql.PreparedStatement

class RegisterController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

   
    import Models.userModel
    import Models.userModelObj._
    import Models.userModelObj.formRegister
    import DataBaseController._
    private val postUrl = routes.RegisterController.createUser
    
    def register = Action { implicit request:  MessagesRequest[AnyContent] =>
    Ok(views.html.register(formRegister,postUrl))
  }



    def createUser = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithErrors: Form[DataUsermodel] =>
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      BadRequest(views.html.register(formWithErrors, postUrl))
    }

    val successFunction = { data: DataUsermodel =>
      // This is the good case, where the form was successfully parsed as a Data object.
      insertData(data.userName,data.password,data.email,data.nombre,data.apellido)
      Redirect(routes.HomeController.index()).flashing("info" -> "UserSuccefullRegister!")
    }

    val formValidationResult = formRegister.bindFromRequest()
    formValidationResult.fold(errorFunction, successFunction)
  }

  def insertData(userName: String, password: String, email: String, nombre: String, apellido: String): Unit = {
  database.withConnection { conn =>
    val statement = conn.prepareStatement(
      """
        |INSERT INTO public."Users"(
        |	userid, "userName", password, email, nombre, "Apellido")
        |	VALUES (DEFAULT, ?, ?, ?, ?, ?)
      """.stripMargin
    )
    statement.setString(1, userName)
    statement.setString(2, password)
    statement.setString(3, email)
    statement.setString(4, nombre)
    statement.setString(5, apellido)
    statement.executeUpdate()
    statement.close()
  }
}



}