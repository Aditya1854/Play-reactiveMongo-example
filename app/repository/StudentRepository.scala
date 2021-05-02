package repository

import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.api.Cursor
import reactivemongo.api.bson.{BSONDocument, document}
import models.Student
import utils.BsonFormat.{studentReader, studentWriter}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.collection.BSONCollection
import javax.inject.Inject


class StudentRepository @Inject()(
                                   implicit executionContext: ExecutionContext,
                                   reactiveMongoApi: ReactiveMongoApi
                                 )  {
  def studentQuery: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("student"))

  def findAll(limit:Int = -1): Future[Seq[Student]]= {
    studentQuery.flatMap(
      _.find(BSONDocument(), Option.empty[Student])
        .cursor[Student]()
        .collect[Seq](limit, Cursor.FailOnError[Seq[Student]]())
    )
  }
  def createStudent(student:Student) = {
    println("\n/\n/\n/\n/\n/ created \n/\n/\n/\n/\n/")
    studentQuery.flatMap(_.insert.one(student))
  }

  def updateStudent(student:Student)= {
    val selector = document("_id" -> student._id)
    // Update the matching student
    studentQuery.flatMap(_.update.one(selector, student))
  }
  def deleteStudent(id:Int)= {
    val selector = document("_id" -> id)
    // delete the matching student
    studentQuery.flatMap(_.delete.one(selector))
  }

}