package Models


case class noteModel(noteName: String, bodyNote: String, userId: Int, noteId: Int)

object noteModelObj{
  import play.api.data.Forms._
  import play.api.data.Form

  case class DataNotemodel(noteName: String, bodyNote: String)

  val formNotes = Form(
    mapping(
      "noteName" -> nonEmptyText,
      "bodyNote" -> nonEmptyText
    )(DataNotemodel.apply)(DataNotemodel.unapply)
  )
}