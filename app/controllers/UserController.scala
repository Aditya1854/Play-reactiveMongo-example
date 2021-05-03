package controllers
import com.google.inject.Inject

import javax.inject._
import play.api.mvc._
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}
import models._
import play.api.libs.json.JsValue
import repository.UserRepository
import utilities.JwtUtility
import utils.JsonFormat._

@Singleton
class UserController @Inject()(cc: ControllerComponents,auth : JwtUtility,userRepository: UserRepository)
                              (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def create():Action[JsValue] = Action.async(controllerComponents.parsers.json) { implicit request => {

    request.body.validate[User].fold(
      _ => Future.successful(BadRequest("Cannot parse request body")),
      user =>
        userRepository.createUser(user).map {
          res => Created(Json.toJson(res.ok)).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
    )
  }}

  def findOne():Action[JsValue] = {
    Action.async(parse.json) { request =>
    request.body.validate[UserData].fold(
      error =>{println(error); Future.successful(BadRequest("Cannot parse request body"))},
      userData =>
        userRepository.findOne(userData.email,userData.password).map {
          case Some(user) =>
            Ok(Json.toJson(Map("token" -> auth.encodeToken(UserName(user.firstName,user.email))))).withHeaders("Access-Control-Allow-Origin" -> "*")
          case None =>
            BadRequest("Email does not exist").withHeaders("Access-Control-Allow-Origin" -> "*")
        }
    )
  }}

}
