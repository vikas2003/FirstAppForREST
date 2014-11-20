package controllers.user

import org.specs2.mutable.Specification
import play.api.http.Status.{ CREATED, NO_CONTENT, NOT_FOUND }
import scala.concurrent.Future
import play.api.mvc.Result
import org.mockito.Mockito._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.libs.json.Json
import services.user.UserServiceComponent
import play.api.test.WithApplication
import domain.user.User
import play.api.test.FakeApplication

class UserControllerSpecs2Unit extends Specification {

  val createEmail: String = "abc@test.com"
  val updateEmail: String = "foo@test.com"

  val id: Long = 1L

  val createUserRequest = FakeRequest(POST, "/users").withBody(Json.obj("email" -> createEmail))
  val updateUserRequest = FakeRequest(PUT, s"/users/$id").withBody(Json.obj("email" -> updateEmail))
  val findUserRequest = FakeRequest(GET, s"/users/$id")
  val deleteUserRequest = FakeRequest(DELETE, s"/users/$id")

  val userController = new UserController with UserServiceComponentMock {}

  "UserController" should {

    "create a new user for POST request" in new WithApplication {
      when(userController.userService.createUser(User(Option.empty, createEmail))).thenReturn(User(Some(1), createEmail))
      val create: Future[Result] = userController.createUser(createUserRequest)
      status(create) must equalTo(CREATED)
      verify(userController.userService).createUser(User(Option.empty, createEmail))
    }

    "update user correctly with PUT request" in new WithApplication {
      when(userController.userService.updateUser(User(Some(id), updateEmail))).thenReturn(true)
      val update: Future[Result] = userController.updateUser(id)(updateUserRequest)

      status(update) must equalTo(NO_CONTENT)
      verify(userController.userService).updateUser(User(Some(id), updateEmail))
    }

    "fail to update non-existent user" in new WithApplication {
      when(userController.userService.updateUser(User(Some(id), updateEmail))).thenReturn(false)
      val update: Future[Result] = userController.updateUser(id)(updateUserRequest)

      status(update) must equalTo(NOT_FOUND)
      verify(userController.userService, times(2)).updateUser(User(Some(id), updateEmail))
    }

    "retrieve a user for given id" in new WithApplication {
      when(userController.userService.tryFindById(id)).thenReturn(Some(User(Some(id), updateEmail)))
      val find: Future[Result] = userController.findUserById(id)(findUserRequest)
      val content = contentAsString(find)
      status(find) must equalTo(OK)
      content must contain(updateEmail)
      content must contain(id.toString)
      verify(userController.userService).tryFindById(id)
    }
  }

}

trait UserServiceComponentMock extends UserServiceComponent {

  override val userService = mock(classOf[UserService])

}