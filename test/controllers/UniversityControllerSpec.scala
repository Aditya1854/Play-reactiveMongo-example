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

class UniversityControllerSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerTest {

  implicit val mockedRepo: UniversityRepository = mock[UniversityRepository]

  "UniversityController " should {

    "get all list of university" in new WithUniversityApplication() {
      val university = Seq(University(1, "hcu", "hyderabad"))
      when(mockedRepo.findAll()) thenReturn Future.successful(university)
      val result = universityController.findAll().apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
//      if you are not usimg JWT then
//      val resultAsString = contentAsString(result)
//      resultAsString mustBe """[{"_id":1,"name":"hcu","location":"hyderabad"}]"""
    }

    "get all list with number of students" in new WithUniversityApplication() {
      val university = List(UniversityJoin(1, "hcu", "hyderabad",Array(Student(1,"aditya","aditya@gmail.com",1,"1995-02-03"))))
      when(mockedRepo.universityAndNumberOfStudents()) thenReturn Future.successful(university)
      val result = universityController.findAll().apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "search list of university with it's name" in new WithUniversityApplication() {
      val university = List(UniversityJoin(1, "hcu", "hyderabad",Array(Student(1,"aditya","aditya@gmail.com",1,"1995-02-03"))))
      when(mockedRepo.universityAndNumberOfStudents()) thenReturn Future.successful(university)
      val result = universityController.search("hcu").apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "create university" in new WithUniversityApplication() {
      val university = University(1, "hcu", "hyderabad")
      when(mockedRepo.createUniversity(university)) thenReturn Future.successful(DefaultWriteResult(true,1,List(),None,None,None))
      val result = universityController.create().apply(FakeRequest().withBody(Json.toJson(university)))
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "update university" in new WithUniversityApplication() {
      val university = University(1, "hcu", "hyderabad")
      when(mockedRepo.updateUniversity(university)) thenReturn Future.successful(UpdateWriteResult(true,1,1,List(),List(),None,None,None))
      val result = universityController.update().apply(FakeRequest().withBody(Json.toJson(university)))
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

    "Delete university" in new WithUniversityApplication() {
      when(mockedRepo.deleteUniversity(1)) thenReturn Future.successful(DefaultWriteResult(true,1,List(),None,None,None))
      val result = universityController.delete(1).apply(FakeRequest())
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) must include("Token is missing")
    }

  }
}

class WithUniversityApplication(implicit mockedRepo: UniversityRepository) extends WithApplication with Injecting {

  implicit val ec = inject[ExecutionContext]
  implicit val security : SecureUser = inject[SecureUser]
  val universityController: UniversityController =
    new UniversityController(
      stubControllerComponents(),
      security,
      mockedRepo
    )

}
