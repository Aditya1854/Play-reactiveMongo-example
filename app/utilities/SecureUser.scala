package utilities
import javax.inject.Inject
import models._
import play.api.http.HeaderNames
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


case class UserRequest[A](user: UserName, token: String, request: Request[A]) extends WrappedRequest[A](request)

class SecureUser @Inject()(bodyParser: BodyParsers.Default, authService: JwtUtility)(implicit ec: ExecutionContext) extends ActionBuilder[UserRequest, AnyContent] {
  private val headerTokenRegex = """Bearer (.+?)""".r

  def parser: BodyParser[AnyContent] = bodyParser

  def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] =
    extractBearerToken(request) match {
      case Some(token) =>
        authService.validateToken(token) match {
          case Right(user) =>
            block(UserRequest(user, token, request))
          case Left(message) =>
            Future.successful(Results.Unauthorized(message))
        }
      case None =>
        Future.successful(Results.Unauthorized("Token is missing"))
    }
  private def extractBearerToken[A](request: Request[A]): Option[String] =
    request.headers.get(HeaderNames.AUTHORIZATION) collect {
      case headerTokenRegex(token) => token
    }

  protected def executionContext: ExecutionContext = ec
}
