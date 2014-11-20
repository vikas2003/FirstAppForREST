package repositories.user

import scala.collection.mutable.HashMap
import java.util.concurrent.atomic.AtomicLong

object UserActions {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  val users = new HashMap[Long, User]             //> users  : scala.collection.mutable.HashMap[Long,repositories.user.UserActions
                                                  //| .User] = Map()
  val idSequence = new AtomicLong(0)              //> idSequence  : java.util.concurrent.atomic.AtomicLong = 0
  
case class User(val id: Option[Long],
                val email: String)

   def createUser(user: User): User = {
      val newId = idSequence.incrementAndGet()
      val createdUser = user.copy(id = Option(newId))
      users.put(newId, createdUser)
      createdUser
    }                                             //> createUser: (user: repositories.user.UserActions.User)repositories.user.User
                                                  //| Actions.User

    def updateUser(user: User): Boolean = {
      if (users.contains(user.id.get)) {
        users.put(user.id.get, user)
        true
      } else false
    }                                             //> updateUser: (user: repositories.user.UserActions.User)Boolean

    def tryFindById(id: Long): Option[User] = {
      users.get(id)
    }                                             //> tryFindById: (id: Long)Option[repositories.user.UserActions.User]

    def delete(id: Long) {
      users.remove(id)
    }                                             //> delete: (id: Long)Unit

createUser(User(Option.empty, "abc@test.com"))    //> res0: repositories.user.UserActions.User = User(Some(1),abc@test.com)

updateUser(User(Some(1), "foo@test.com"))         //> res1: Boolean = true

tryFindById(1)                                    //> res2: Option[repositories.user.UserActions.User] = Some(User(Some(1),foo@tes
                                                  //| t.com))
}