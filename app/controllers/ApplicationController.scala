package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.Inject

class ApplicationController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def preflight(all: String): Action[AnyContent] = Action {req =>
    println("validate origin ...." + req)
    Ok("").withHeaders(
      "Access-Control-Allow-Origin" -> "*",
      "Allow" -> "*",
      "Access-Control-Allow-Methods" -> "*",
      "Access-Control-Allow-Headers" ->
        "*")
  }

}
