package controllers.user

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import domain.user.User
import services.user.UserServiceComponent
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


trait UserController extends Controller {
  self: UserServiceComponent =>
  implicit val userReads = (__ \ "email").read[String].map(resource => UserResource(resource))
  
  implicit val userWrites = new Writes[User] {
    override def writes(user: User): JsValue = {
      Json.obj("id" -> user.id,
        "email" -> user.email)
    }
  }
  def createUser = Action(parse.json) { request =>
    unmarshalUserResource(request, (resource: UserResource) => {
      val user = User(10,
        resource.email, None)
      userService.createUser(user)
      Created
    })
  }
  def updateUser(id: Int) = Action(parse.json) { request =>
    unmarshalUserResource(request, (resource: UserResource) => {
      val user = User(id, resource.email, None)
      if (userService.updateUser(user)) NoContent
      else NotFound
    })
  }
  def findUserById(id: Int) = Action {
    val user = userService.tryFindById(id)
    
    if (user != null && user.isDefined) {
      Ok(Json.toJson(user))
    } else {
      NotFound
    }
  }
  
  def findUserFieldsById(id: Int, fields: Option[String]) = Action {
    val user = userService.tryFindById(id)
    
    println("fields: " + fields)
    
    if (user != null && user.isDefined) {
      Ok(Json.toJson(user))
    } else {
      NotFound
    }
  }
  
  def deleteUser(id: Int) = Action {
    userService.delete(id)
    NoContent
  }
  private def unmarshalUserResource(request: Request[JsValue], block: (UserResource) => Result): Result = {
    request.body.validate[UserResource].fold(
      valid = block,
      invalid = (e => {
        val error = e.mkString
        Logger.error(error)
        BadRequest(error)
      }))
  }
}
case class UserResource(val email: String)