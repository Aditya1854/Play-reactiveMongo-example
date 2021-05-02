package utils
import models._
import play.api.libs.json.{Json, OFormat}
import reactivemongo.api.bson.{BSONDocumentReader, BSONDocumentWriter, Macros}

object BsonFormat {
  implicit def studentWriter: BSONDocumentWriter[Student] = Macros.writer[Student]
  implicit def studentReader: BSONDocumentReader[Student] = Macros.reader[Student]

  implicit def studentJoinWriter: BSONDocumentWriter[StudentJoin] = Macros.writer[StudentJoin]
  implicit def studentJoinReader: BSONDocumentReader[StudentJoin] = Macros.reader[StudentJoin]

  implicit def universityWriter: BSONDocumentWriter[University] = Macros.writer[University]
  implicit def universityReader: BSONDocumentReader[University] = Macros.reader[University]

  implicit def universityJoinWriter: BSONDocumentWriter[UniversityJoin] = Macros.writer[UniversityJoin]
  implicit def universityJoinReader: BSONDocumentReader[UniversityJoin] = Macros.reader[UniversityJoin]

  implicit def userWriter: BSONDocumentWriter[User] = Macros.writer[User]
  implicit def userReader: BSONDocumentReader[User] = Macros.reader[User]

  implicit def userDataWriter: BSONDocumentWriter[UserData] = Macros.writer[UserData]
  implicit def userDataReader: BSONDocumentReader[UserData] = Macros.reader[UserData]

  implicit def userNameWriter: BSONDocumentWriter[UserName] = Macros.writer[UserName]
  implicit def userNameReader: BSONDocumentReader[UserName] = Macros.reader[UserName]
}
object JsonFormat{
  implicit def studentJson: OFormat[Student] = Json.format[Student]
  implicit def universityJson: OFormat[University] = Json.format[University]

  implicit def studentDataJson: OFormat[StudentData] = Json.format[StudentData]
  implicit def universityDataJson: OFormat[UniversityData] = Json.format[UniversityData]

  implicit def studentJoinJson: OFormat[StudentJoin] = Json.format[StudentJoin]
  implicit def universityJoinJson: OFormat[UniversityJoin] = Json.format[UniversityJoin]

  implicit def userJson: OFormat[User] = Json.format[User]
  implicit def userDataJson: OFormat[UserData] = Json.format[UserData]
  implicit def userNameJson: OFormat[UserName] = Json.format[UserName]
}
