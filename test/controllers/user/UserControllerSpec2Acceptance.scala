package controllers.user

import org.specs2.Specification
import play.api.test._
import scala.concurrent.Future
import play.api.test.Helpers._
import domain.user.User
import services.user.UserServiceComponent
import play.api.libs.json.Json

class UserControllerSpecs2Acceptance extends Specification {

  val createEmail: String = "abc@test.com"
  val updateEmail: String = "foo@test.com"

  val id: Long = 1L

  val createUserRequest = FakeRequest(POST, "/users").withBody(Json.obj("email" -> createEmail))
  val updateUserRequest = FakeRequest(PUT, s"/users/$id").withBody(Json.obj("email" -> updateEmail))
  val updateUserNFRequest = FakeRequest(PUT, s"/users/14").withBody(Json.obj("email" -> updateEmail))
  val findUserRequest = FakeRequest(GET, s"/users/$id")
  val findUserNFRequest = FakeRequest(GET, s"/users/14")
  val deleteUserRequest = FakeRequest(DELETE, s"/users/$id")

  def is = s2"""
  
  When application receives a request
    to create a user, a new user must be created $verifyCreateUser
    to update prev user, user must be updated $verifyUpdateUser
    to update any user, return a NOT_FOUND $verifyUpdateUserNotFound
    to find a user by id, return that user $verifyFindUser
    to find a user by incorrect id, return NOT_FOUND $verifyFindUserNF
    to delete a user by id, delete the user $verifyDeleteUser
  """

  def verifyDeleteUser = {
    running(FakeApplication()) {
      status(route(deleteUserRequest).get) must equalTo(NO_CONTENT)
    }
  }

  def verifyCreateUser = {
    running(FakeApplication()) {
      status(route(createUserRequest).get) must equalTo(CREATED)
    }
  }

  def verifyUpdateUser = {
    running(FakeApplication()) {
      status(route(updateUserRequest).get) must equalTo(NO_CONTENT)
    }
  }

  def verifyUpdateUserNotFound = {
    running(FakeApplication()) {
      status(route(updateUserNFRequest).get) must equalTo(NOT_FOUND)
    }
  }

  def verifyFindUser = {
    running(FakeApplication()) {
      val findUser = route(findUserRequest).get
      val bodyText = contentAsString(findUser)
      status(findUser) must equalTo(OK) and
        (bodyText contains (updateEmail)) and
        (bodyText contains (id.toString))
    }
  }

  def verifyFindUserNF = {
    running(FakeApplication()) {
      val findUser = route(findUserNFRequest).get
      status(findUser) must equalTo(NOT_FOUND)
    }
  }
}
