package controllers

import javax.inject.Inject

import play.api.data._
import play.api.i18n._
import play.api.mvc._

import java.sql.Connection
import play.api.db.Database
import java.sql.PreparedStatement

class LoginController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

    import Models.loginModelObj._
    import Models.loginModelObj.formLogin
    import DataBaseController._

    val postUrl = routes.LoginController.authUser



    def login = Action { implicit request:  MessagesRequest[AnyContent] =>
    Ok(views.html.login(formLogin,postUrl))
  }

  def authUser = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithErrors: Form[LoginUsermodel] =>
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      BadRequest(views.html.login(formWithErrors, postUrl))
    }

    val successFunction = { data: LoginUsermodel =>
      // This is the good case, where the form was successfully parsed as a Data object.
      val userId = selecData(data.userName, data.password) 
      if( userId != " "){
        val session = request.session + ("userId"->userId)
      Redirect("/notes").withSession(session).flashing("info" -> "UserSuccefullRegister!")
      }else{
        Redirect(routes.HomeController.index()).flashing("info" -> "Error UserNotFound!")
      }
    }

    val formValidationResult = formLogin.bindFromRequest()
    formValidationResult.fold(errorFunction, successFunction)
  }



    def selecData(username: String, password: String): String ={
        var result = " "
    database.withConnection { conn =>
    val statement = conn.prepareStatement(
      s"""
        SELECT userid, "userName", password, email, nombre, "Apellido"
        FROM public."Users" WHERE "userName" = ? AND password = ? ;
      """
    )
    statement.setString(1, username)
    statement.setString(2, password)
    val resultSet = statement.executeQuery()
    if(resultSet.next()){
        val userid = resultSet.getInt("userid")
        result = userid.toString
    }else{
        result = " "
    }
    resultSet.close()
    statement.close()
    result
  }
}

    def logout = Action { implicit request =>
  Redirect(routes.HomeController.index()).withNewSession.flashing("info" -> "UserSuccessfullyLoggedOut!")
    }
}