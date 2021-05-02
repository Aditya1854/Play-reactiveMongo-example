package utilities
import models._
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}
import play.api.libs.json.Json
import utils.JsonFormat._
import java.time.Clock
import scala.util.{Failure, Success}


class JwtUtility {
  private val expiresIn = 1 * 24 * 60 * 60
  implicit val clock: Clock = Clock.systemUTC
  val secretKey = "topsecreeeetofproject10@fs"
  val JwtSecretAlgo = "HS256"

  def encodeToken(name: UserName): String = {
    Jwt.encode(JwtClaim(Json.prettyPrint(Json.toJson(name))).issuedNow.expiresIn(expiresIn), secretKey, JwtAlgorithm.HS256)
  }

  def validateToken(jwtToken: String): Either[String, UserName] = {
    Jwt.decode(jwtToken, secretKey, Seq(JwtAlgorithm.HS256)) match {
      case Success(value) =>
        Right(Json.parse(value.content).as[UserName])
      case Failure(ex) =>
        Left(ex.getMessage)
    }
  }

}

object JwtUtility extends JwtUtility
