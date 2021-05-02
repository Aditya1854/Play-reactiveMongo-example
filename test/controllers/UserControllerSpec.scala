package controllers
import models._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication, _}
import repository.UserRepository
import utils.JsonFormat._
import org.mockito.MockitoSugar
import reactivemongo.api.commands.DefaultWriteResult
import utilities.JwtUtility

import scala.concurrent.{ExecutionContext, Future}
class UserControllerSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerTest{
  implicit val mockedRepo: UserRepository = mock[UserRepository]

  "UserController " should {

    "create user" in new WithUserApplication() {
      val user = User("aman","kumar","aman@gmail.com","123456")
      when(mockedRepo.createUser(user)) thenReturn Future.successful(DefaultWriteResult(true,1,List(),None,None,None))
      val result = userController.create().apply(FakeRequest().withBody(Json.toJson(user)))
      status(result) mustBe CREATED
      val resultAsString = contentAsString(result)
      resultAsString mustBe """true"""
    }

    "validate user" in new WithUserApplication() {
      when(mockedRepo.findOne("aditya@gmail.com","123456")) thenReturn Future.successful(Some(User("Aditya","Kumar","aditya@gmail.com","123456")))
      val result = userController.findOne().apply(FakeRequest().withBody(Json.toJson(UserData("aditya@gmail.com","123456"))))
      status(result) mustBe OK
     val resultAsString = contentAsString(result)
      println(resultAsString)
//   resultAsString mustBe """{"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjAwNzIxODEsImlhdCI6MTYxOTk4NTc4MSwKICAiZmlyc3ROYW1lIiA6ICJBZGl0eWEiLAogICJlbWFpbCIgOiAiYWRpdHlhQGdtYWlsLmNvbSIKfQ.Pwg_0qkrH3WgIZzX3AAzX890Ue_0a21aOGl7vRW_Q_M"}"""
    }

  }

}

class WithUserApplication(implicit mockedRepo: UserRepository) extends WithApplication with Injecting {

  implicit val ec = inject[ExecutionContext]
  implicit val jwt : JwtUtility = inject[JwtUtility]
  val userController: UserController =
    new UserController(
      stubControllerComponents(),
      jwt,
      mockedRepo
    )

}

