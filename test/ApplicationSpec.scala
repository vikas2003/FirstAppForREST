import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import play.libs.Scala
import play.api.libs.json._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "render the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("RESTful API written in Scala, using the Play Framework.")
    }

   /* "create user with email foo@test.com then update email to abc@test.com" in new WithApplication {
      val email = "foo@test.com"
      val createUser = FakeRequest(POST, "/users")
        .withBody(
          Json.obj(
            "email" -> email))
      val create = route(createUser).get

      status(create) must equalTo(CREATED)//201

      val id = 1
      val newEmail = "abc@test.com"
      val updateUser = FakeRequest(PUT, s"/users/$id")
        .withBody(
          Json.obj(
            "email" -> newEmail))
       val update = route(updateUser).get
       
       status(update) must equalTo(NO_CONTENT)//204
    }*/

  }
}
