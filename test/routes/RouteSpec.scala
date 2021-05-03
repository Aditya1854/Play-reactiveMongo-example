package routes

import models._
import org.h2.jdbc.JdbcSQLException
import org.mockito.MockitoSugar
import org.scalatest.concurrent.ScalaFutures.whenReady
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import reactivemongo.api.commands.{DefaultWriteResult, UpdateWriteResult}
import repository.{StudentRepository, UniversityRepository, UserRepository}
import utils.JsonFormat._

import scala.concurrent.Future

class RouteSpec extends PlaySpec with GuiceOneAppPerSuite  with MockitoSugar {
  implicit val mockedRepo: StudentRepository = mock[StudentRepository]
  implicit val mockedRepoUniversity: UniversityRepository = mock[UniversityRepository]
  implicit val mockedRepoUser: UserRepository = mock[UserRepository]
  "Routes" should {

    /****
      ***student route test

      */

    "get all list of Student from route" in new WithApplication {
      val student = Seq(Student(1,"aditya","aditya@gmail.com",1,"1995-05-06"))
      when(mockedRepo.findAll()) thenReturn Future.successful(student)
      val result =  route(app, FakeRequest(GET, "/student/")).get
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "get all list with university name from route" in new WithApplication{
      val student = List(StudentJoin(1,"aditya","aditya@gmail.com",1,"1995-02-03",Array(University(1, "hcu", "hyderabad"))))
      when(mockedRepoUniversity.studentByUniversityName()) thenReturn Future.successful(student)
      val result = route(app, FakeRequest(GET, "/student/list")).get
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "search list of student with it's name from route" in new WithApplication {
      val student = List(StudentJoin(1,"aditya","aditya@gmail.com",1,"1995-02-03",Array(University(1, "hcu", "hyderabad"))))
      when(mockedRepoUniversity.studentByUniversityName()) thenReturn Future.successful(student)
      val result = route(app, FakeRequest(GET, "/student/search")).get
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "create student from route" in new WithApplication {
      val student= Student(1,"aditya","aditya@gmail.com",1,"1995-05-06")
      when(mockedRepo.createStudent(student)) thenReturn Future.successful(DefaultWriteResult(true,1,List(),None,None,None))
      val result = route(app, FakeRequest(GET, "/student/").withBody(Json.toJson(student))).get
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "update student from route" in new WithApplication {
      val student= Student(1,"aditya","aditya@gmail.com",1,"1995-05-06")
      when(mockedRepo.updateStudent(student)) thenReturn Future.successful(UpdateWriteResult(true,1,1,List(),List(),None,None,None))
      val result = route(app, FakeRequest(GET, "/student/").withBody(Json.toJson(student))).get
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "Delete student from route" in new WithApplication {
      when(mockedRepo.deleteStudent(1)) thenReturn Future.successful(DefaultWriteResult(true,1,List(),None,None,None))
      val result = route(app, FakeRequest(GET, "/student/1")).get
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

/****
***University route test

 */

    "get all list of university from route" in new WithApplication {
      val university = Seq(University(1, "hcu", "hyderabad"))
      when(mockedRepoUniversity.findAll()) thenReturn Future.successful(university)
      val result = route(app, FakeRequest(GET, "/university/")).get
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "get all list with number of students from route" in new WithApplication {
      val university = List(UniversityJoin(1, "hcu", "hyderabad",Array(Student(1,"aditya","aditya@gmail.com",1,"1995-02-03"))))
      when(mockedRepoUniversity.universityAndNumberOfStudents()) thenReturn Future.successful(university)
      val result = route(app, FakeRequest(GET, "/university/list")).get
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "search list of university with it's name from route" in new WithApplication {
      val university = List(UniversityJoin(1, "hcu", "hyderabad",Array(Student(1,"aditya","aditya@gmail.com",1,"1995-02-03"))))
      when(mockedRepoUniversity.universityAndNumberOfStudents()) thenReturn Future.successful(university)
      val result = route(app, FakeRequest(GET, "/university/search")).get
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "create university from route" in new WithApplication {
      val university = University(1, "hcu", "hyderabad")
      when(mockedRepoUniversity.createUniversity(university)) thenReturn Future.successful(DefaultWriteResult(true,1,List(),None,None,None))
      val result = route(app, FakeRequest(GET, "/university/").withBody(Json.toJson(university))).get
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "update university from route" in new WithApplication {
      val university = University(1, "hcu", "hyderabad")
      when(mockedRepoUniversity.updateUniversity(university)) thenReturn Future.successful(UpdateWriteResult(true,1,1,List(),List(),None,None,None))
      val result = route(app, FakeRequest(GET, "/university/").withBody(Json.toJson(university))).get
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "Delete university from route" in new WithApplication {
      when(mockedRepoUniversity.deleteUniversity(1)) thenReturn Future.successful(DefaultWriteResult(true,1,List(),None,None,None))
      val result = route(app, FakeRequest(GET, "/university/1")).get
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    /****
      ***User route test

      */

    "create user" in new WithApplication{
      val user = User("aman","kumar","aman@gmail.com","123456")
      when(mockedRepoUser.createUser(user)) thenReturn Future.successful(DefaultWriteResult(true,1,List(),None,None,None))
      val result = route(app, FakeRequest(POST, "/user/").withBody(Json.toJson(user))).get
      status(result) mustBe CREATED
      val resultAsString = contentAsString(result)
      resultAsString mustBe """true"""
    }

    "validate user" in new WithApplication {
      when(mockedRepoUser.findOne("aditya@gmail.com","123456")) thenReturn Future.successful(Some(User("Aditya","Kumar","aditya@gmail.com","123456")))
      val result = route(app, FakeRequest(POST, "/users/").withBody(Json.toJson(UserData("aditya@gmail.com","123456")))).get
      status(result) mustBe OK
      val resultAsString = contentAsString(result)
      println(resultAsString)
    }

  }
}
