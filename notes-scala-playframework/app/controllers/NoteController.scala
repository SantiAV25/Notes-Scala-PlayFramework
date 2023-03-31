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
    import DataBaseController._

    var notesArr: mutable.ArrayBuffer[noteModel] = mutable.ArrayBuffer.empty[noteModel]

    def notes = Action { implicit request =>
  request.session.get("userId") match {
    case Some(userId) => {
      // User is authenticated, render notes page
      selecData(userId)
      Ok(views.html.createNote(notesArr.toSeq,userId))
    }
    case None => {
      // User is not authenticated, redirect to login page
      Redirect("/").flashing("info" -> "Please log in.")
    }
  }
}

     def selecData(userId: String): Unit ={
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
