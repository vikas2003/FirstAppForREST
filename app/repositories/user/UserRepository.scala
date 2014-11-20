package repositories.user

import domain.user.User
import java.util.concurrent.atomic.AtomicLong

trait UserRepositoryComponent {
  val userRepository: UserRepository

  trait UserRepository {

    def createUser(user: User): User

    def updateUser(user: User): Boolean

    def tryFindById(id: Long): Option[User]

    def delete(id: Long)

  }
}

trait UserRepositoryComponentImpl extends UserRepositoryComponent {
  override val userRepository = new UserRepositoryImpl

  class UserRepositoryImpl extends UserRepository {

    var users = Map[Long, User]()
    var idSequence = new AtomicLong(0)

    override def createUser(user: User): User = {
      val newId = idSequence.incrementAndGet()
      val createdUser = user.copy(id = Option(newId))
      users += (newId -> createdUser)
      createdUser
    }

    override def updateUser(user: User): Boolean = {
      if (users.contains(user.id.get)) {
        users += (user.id.get -> user)
        true
      } else false
    }

    override def tryFindById(id: Long): Option[User] = {
      users.get(id)
    }

    override def delete(id: Long) {
      users -= (id)
    }
  }
}