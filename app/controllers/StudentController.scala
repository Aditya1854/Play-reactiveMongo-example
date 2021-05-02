package controllers

import com.google.inject.Inject

import javax.inject._
import play.api.mvc._
import play.api.libs.json.{Json, __}

import scala.concurrent.{ExecutionContext, Future}
import models.{Student, StudentData}
import play.api.libs.json.JsValue
import repository.{StudentRepository, UniversityRepository}
import utilities.SecureUser
import utils.JsonFormat._


@Singleton
class StudentController @Inject()(cc: ControllerComponents,security : SecureUser,studentRepository: StudentRepository,university : UniversityRepository)
                                 (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def findAll():Action[AnyContent] = security.async { implicit request: Request[AnyContent] =>
    studentRepository.findAll().map(student => Ok(Json.toJson(student)).withHeaders("Access-Control-Allow-Origin" -> "*"))
  }

  def create():Action[JsValue] = security.async(controllerComponents.parsers.json) { implicit request => {

    request.body.validate[Student].fold(
      _ => Future.successful(BadRequest("Cannot parse request body")),
      student =>
        studentRepository.createStudent(student).map {
          _ => Created(Json.toJson(student)).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
    )
  }}

  def update():Action[JsValue]  = security.async(controllerComponents.parsers.json) { implicit request => {
    request.body.validate[Student].fold(
      _ => Future.successful(BadRequest("Cannot parse request body")),
      student =>
        studentRepository.updateStudent(student).map {
            result => Ok(Json.toJson(result.ok)).withHeaders("Access-Control-Allow-Origin" -> "*")
          })
  }}

  def delete(id: Int):Action[AnyContent]  =security.async { implicit request => {
    studentRepository.deleteStudent(id).map {
        res => Ok(Json.toJson(res.ok)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }

  }}

  def studentByUniversityName():Action[AnyContent] = security.async { implicit request: Request[AnyContent] =>
    university.studentByUniversityName().map(res => {
      val result = for(x <- res) yield StudentData(x._id,x.name,x.email,x.universityDetails(0).name,x.DOB)
      Ok(Json.toJson(result)).withHeaders("Access-Control-Allow-Origin" -> "*")
    } )
  }
  def search(name : String):Action[AnyContent] = security.async { implicit request: Request[AnyContent] =>
    university.studentByUniversityName().map(res => {
      val filteredResult = res.filter(_.name == name)
      val result = for(x <- filteredResult) yield StudentData(x._id,x.name,x.email,x.universityDetails(0).name,x.DOB)
      Ok(Json.toJson(result)).withHeaders("Access-Control-Allow-Origin" -> "*")
    } )
  }

}

