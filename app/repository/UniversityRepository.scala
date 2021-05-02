package repository
import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.api.Cursor
import reactivemongo.api.bson.{BSONDocument, document}
import models._
import utils.BsonFormat._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.commands.WriteResult

import javax.inject.Inject

class UniversityRepository @Inject()(
                                      implicit executionContext: ExecutionContext,
                                      reactiveMongoApi: ReactiveMongoApi,
                                      student : StudentRepository
                                    )  {
  def universityQuery: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("university"))
  def findAll(limit:Int = -1): Future[Seq[University]]= {
    universityQuery.flatMap(
      _.find(BSONDocument(), Option.empty[University])
        .cursor[University]()
        .collect[Seq](limit, Cursor.FailOnError[Seq[University]]())
    )
  }

  def createUniversity(university:University): Future[WriteResult] =
    universityQuery.flatMap(_.insert.one(university))

  def updateUniversity(university:University)= {
    val selector = document("_id" -> university._id)
    // Update the matching University
    universityQuery.flatMap(_.update.one(selector, university))
  }

  def deleteUniversity(id:Int): Future[WriteResult]= {
    val selector = document("_id" -> id)
    // Delete the matching University
    universityQuery.flatMap(_.delete.one(selector))
  }
def studentByUniversityName() = {

  def find(university: BSONCollection,student: BSONCollection) = {
    import student.aggregationFramework.Lookup
    student.aggregatorContext[StudentJoin](
      Lookup(university.name, "universityId","_id", "universityDetails")
    ).prepared.cursor.
      collect[List](-1, Cursor.FailOnError[List[StudentJoin]]())
  }
  universityQuery.flatMap(x => student.studentQuery.flatMap(y => find(x,y)))
}

//UNIVERSITY
  def universityAndNumberOfStudents(): Future[List[UniversityJoin]] = {
    def find(student: BSONCollection, university: BSONCollection):Future[List[UniversityJoin]] = {
      import university.aggregationFramework.Lookup
      university.aggregatorContext[UniversityJoin](
        Lookup(student.name, "_id","universityId", "students")
      ).prepared.cursor.
        collect[List](-1, Cursor.FailOnError[List[UniversityJoin]]())
    }
    universityQuery.flatMap(x => student.studentQuery.flatMap(y =>  find(y,x)))
  }
}
