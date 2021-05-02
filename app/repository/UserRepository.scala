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

class UserRepository @Inject()(
                                implicit executionContext: ExecutionContext,
                                reactiveMongoApi: ReactiveMongoApi,
                                student : StudentRepository
                              ) {
  def userQuery: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("user"))

  def createUser(user:User): Future[WriteResult] = userQuery.flatMap(_.insert.one(user))

  def findOne(email:String,password:String): Future[Option[User]] = {
      userQuery.flatMap(_.find(BSONDocument("email"->email,"password"->password), Option.empty[User]).one[User])
    }
}
