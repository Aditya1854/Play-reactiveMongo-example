package controllers
import models._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.i18n.{DefaultLangs, MessagesApi}
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication, _}
import repository.{StudentRepository, UniversityRepository}
import utils.JsonFormat._
import org.mockito.MockitoSugar
import reactivemongo.api.commands.{DefaultWriteResult, UpdateWriteResult, WriteResult}
import utilities.SecureUser

import scala.concurrent.{ExecutionContext, Future}

class StudentControllerSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerTest {

  implicit val mockedRepo: StudentRepository = mock[StudentRepository]

  implicit val mockedRepoUniversity: UniversityRepository = mock[UniversityRepository]

  "StudentController " should {

    "get all list of Student" in new WithStudentApplication() {
      val student = Seq(Student(1,"aditya","aditya@gmail.com",1,"1995-05-06"))
      when(mockedRepo.findAll()) thenReturn Future.successful(student)
      val result = studentController.findAll().apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "get all list with university name" in new WithStudentApplication() {
      val student = List(StudentJoin(1,"aditya","aditya@gmail.com",1,"1995-02-03",Array(University(1, "hcu", "hyderabad"))))
      when(mockedRepoUniversity.studentByUniversityName()) thenReturn Future.successful(student)
      val result = studentController.studentByUniversityName().apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "search list of student with it's name" in new WithStudentApplication() {
      val student = List(StudentJoin(1,"aditya","aditya@gmail.com",1,"1995-02-03",Array(University(1, "hcu", "hyderabad"))))
      when(mockedRepoUniversity.studentByUniversityName()) thenReturn Future.successful(student)
      val result = studentController.search("aditya").apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

//        "create student" in new WithStudentApplication() {
//          val student= Student(1,"aditya","aditya@gmail.com",1,"1995-05-06")
//          when(mockedRepo.createStudent(student)) thenReturn Future.successful(DefaultWriteResult(true,1,List(),None,None,None))
//          val result = studentController.create().apply(FakeRequest())
//          status(result) mustBe UNAUTHORIZED
//          contentType(result) mustBe Some("text/plain")
//          contentAsString(result) must include("Token is missing")
//        }
//
//        "update university" in new WithStudentApplication() {
//          val student= Student(1,"aditya","aditya@gmail.com",1,"1995-05-06")
//          when(mockedRepo.updateStudent(student)) thenReturn Future.successful(UpdateWriteResult(true,1,1,List(),List(),None,None,None))
//          val result = studentController.update().apply(FakeRequest())
//          status(result) mustBe UNAUTHORIZED
//          contentType(result) mustBe Some("text/plain")
//          contentAsString(result) must include("Token is missing")
//        }

    "Delete university" in new WithUniversityApplication() {
      when(mockedRepo.deleteStudent(1)) thenReturn Future.successful(DefaultWriteResult(true,1,List(),None,None,None))
      val result = universityController.delete(1).apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

  }
}

class WithStudentApplication(implicit mockedRepo: StudentRepository) extends WithApplication with Injecting {

  implicit val ec = inject[ExecutionContext]
  implicit val security : SecureUser = inject[SecureUser]
  implicit val university : UniversityRepository = inject[UniversityRepository]
  val studentController: StudentController =
    new StudentController(
      stubControllerComponents(),
      security,
      mockedRepo,
      university
    )


}
