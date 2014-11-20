package services.user

import org.mockito.Mockito._
import org.junit.Test
import org.junit.Assert._
import repositories.user.UserRepositoryComponent
import domain.user.User
import org.specs2.mutable.Specification
import play.api.test.WithApplication
import play.api.test.WithApplication

class UserServiceTest extends Specification with UserServiceComponentImpl
  with UserRepositoryMockComponent {

  "UserService" should {

    "create user" in new WithApplication {
      val user = User(Option.empty, "foo@test.com")
      when(userRepository.createUser(user)).thenReturn(user.copy(id = Option(1L)))

      val cUser = userService.createUser(user)

      verify(userRepository).createUser(user)
      cUser.id must beSome [Long]
      user.email must equalTo(cUser.email)
    }

    "update known user" in new WithApplication {
      val user = User(Option(1L), "foo@test.com")
      when(userRepository.updateUser(user)).thenReturn(true)

      val uUser = userService.updateUser(user)

      verify(userRepository).updateUser(user)
      uUser must_== true
    }
    
    "update unknown user" in new WithApplication {
      val user = User(Option(1L), "foo@test.com")
      when(userRepository.updateUser(user)).thenReturn(false)

      val uUser = userService.updateUser(user)

      verify(userRepository, times(2)).updateUser(user)
      uUser must_== false
    }

    "find user by known id" in new WithApplication {
      val id = 1L
      val user = User(Option(id), "foo@test.com")
      when(userRepository.tryFindById(id)).thenReturn(Option(user))

      val retrievedUser = userService.tryFindById(id)

      verify(userRepository).tryFindById(id)
      user must equalTo(retrievedUser.get)
    }
    
    "fail to find user for unknown id" in new WithApplication {
      val id = 1L
      when(userRepository.tryFindById(id)).thenReturn(Option.empty)

      val retrievedUser = userService.tryFindById(id)

      verify(userRepository, times(2)).tryFindById(id)
      retrievedUser must beNone
    }
    
    "delete user for given id" in new WithApplication {
      val id = 1L
      userService.delete(id)
      verify(userRepository).delete(id)
    }
  }

}

trait UserRepositoryMockComponent extends UserRepositoryComponent {

  override val userRepository = mock(classOf[UserRepository])

}
