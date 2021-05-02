package controllers
import com.google.inject.Inject

import javax.inject._
import play.api.mvc._
import play.api.libs.json.{Json, __}

import scala.concurrent.{ExecutionContext, Future}
import models.{University, UniversityData}
import play.api.libs.json.JsValue
import repository.UniversityRepository
import utilities.SecureUser
import utils.JsonFormat._


@Singleton
class UniversityController @Inject()(cc: ControllerComponents, security : SecureUser, universityRepository: UniversityRepository)
                                    (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def findAll():Action[AnyContent] = security.async { implicit request: Request[AnyContent] =>
    universityRepository.findAll().map(university => Ok(Json.toJson(university)).withHeaders("Access-Control-Allow-Origin" -> "*"))
  }

  def create():Action[JsValue] = security.async(controllerComponents.parsers.json) { implicit request => {
    request.body.validate[University].fold(
      _ => Future.successful(BadRequest("Cannot parse request body")),
      university =>
        universityRepository.createUniversity(university).map {
          _ => Created(Json.toJson(university)).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
    )
  }}

  def update():Action[JsValue]  = security.async(controllerComponents.parsers.json) { implicit request => {
    request.body.validate[University].fold(
      _ => Future.successful(BadRequest("Cannot parse request body")),
      university =>
        universityRepository.updateUniversity(university).map {
          result => Ok(Json.toJson(result.ok)).withHeaders("Access-Control-Allow-Origin" -> "*")
        })
  }}

  def delete(id: Int):Action[AnyContent]  = security.async { implicit request => {
    universityRepository.deleteUniversity(id).map {
      res => Ok(Json.toJson(res.ok)).withHeaders("Access-Control-Allow-Origin" -> "*")
    }

  }}
  def universityAndNumberOfStudents():Action[AnyContent] = security.async { implicit request: Request[AnyContent] =>
    universityRepository.universityAndNumberOfStudents().map(res => {
      val result = for(x <- res) yield UniversityData(x._id,x.name,x.location,x.students.length)
      Ok(Json.toJson(result)).withHeaders("Access-Control-Allow-Origin" -> "*")
    } )
  }

  def search(name : String):Action[AnyContent] = security.async { implicit request: Request[AnyContent] =>
    universityRepository.universityAndNumberOfStudents().map(res => {
      val filteredResult = res.filter(_.name == name)
      val result = for(x <- filteredResult) yield UniversityData(x._id,x.name,x.location,x.students.length)
      Ok(Json.toJson(result)).withHeaders("Access-Control-Allow-Origin" -> "*")
    } )
  }
}
