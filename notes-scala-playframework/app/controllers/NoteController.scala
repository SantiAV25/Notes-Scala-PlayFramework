package controllers

import javax.inject.Inject

import play.api.data._
import play.api.i18n._
import play.api.mvc._

import java.sql.Connection
import play.api.db.Database
import java.sql.PreparedStatement

import scala.collection._

class NoteController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

    import Models.noteModel
    import Models.noteModelObj._
    import DataBaseController._

    var notesArr: mutable.ArrayBuffer[noteModel] = mutable.ArrayBuffer.empty[noteModel]

    def notes = Action { implicit request =>
  request.session.get("userId") match {
    case Some(userId) => {
      // User is authenticated, render notes page
      selecData(userId)
      Ok(views.html.viewNote(notesArr.toSeq, formNotes,userId))
    }
    case None => {
      // User is not authenticated, redirect to login page
      Redirect("/").flashing("info" -> "Please log in.")
    }
  }
}

def createNote = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithErrors: Form[DataNotemodel] =>
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      BadRequest(views.html.viewNote(notesArr.toSeq,formWithErrors,request.session.get("userId").toString))
    }

    val successFunction = { data: DataNotemodel =>
      // This is the good case, where the form was successfully parsed as a Data object.
       request.session.get("userId") match {
    case Some(userId) => {
      // User is authenticated, render notes page
      insertNote(data.noteName, data.bodyNote ,userId)
      Redirect("/notes").flashing("info" -> "NoteAdd Correctly")
    }
    case None => {
      // User is not authenticated, redirect to login page
      Redirect("/").flashing("info" -> "Please log in.")
    }
  }
    }

    val formValidationResult = formNotes.bindFromRequest()
    formValidationResult.fold(errorFunction, successFunction)
  }

   def insertNote(noteName: String, bodyNote: String, userId: String): Unit = {
    val userIdOpt = userId.headOption
  database.withConnection { conn =>
    val statement = conn.prepareStatement(
      """
        INSERT INTO public."Notes"(
	    userid, "Notesid", "nombreNotes", "cuerpoNotes")
	    VALUES (?, Default, ?, ?);
      """.stripMargin
    )
    statement.setInt(1, userId.toInt)
    statement.setString(2, noteName)
    statement.setString(3, bodyNote)
    statement.executeUpdate()
    statement.close()
  }
}

    

     def selecData(userId: String): Unit ={
        notesArr.clear()
    database.withConnection { conn =>
    val statement = conn.prepareStatement(
      s"""
        SELECT userid, "Notesid", "nombreNotes", "cuerpoNotes"
	    FROM public."Notes" WHERE userid = ?;
      """
    )
    statement.setInt(1, userId.toInt)
    val resultSet = statement.executeQuery()
    while(resultSet.next()){
        val noteAdd = noteModel(noteName = resultSet.getString("nombreNotes"), bodyNote = resultSet.getString("cuerpoNotes"), userId = resultSet.getInt("userid"), noteId = resultSet.getInt("Notesid"))
        notesArr += noteAdd 
    }
    resultSet.close()
    statement.close()
  }
}


  




}
