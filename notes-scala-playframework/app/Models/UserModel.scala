
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