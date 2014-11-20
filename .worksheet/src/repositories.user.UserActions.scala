package repositories.user

import scala.collection.mutable.HashMap
import java.util.concurrent.atomic.AtomicLong

object UserActions {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(178); 
  println("Welcome to the Scala worksheet");$skip(39); 

  val users = new HashMap[Long, User];System.out.println("""users  : scala.collection.mutable.HashMap[Long,repositories.user.UserActions.User] = """ + $show(users ));$skip(37); 
  val idSequence = new AtomicLong(0)
  
case class User(val id: Option[Long],
                val email: String);System.out.println("""idSequence  : java.util.concurrent.atomic.AtomicLong = """ + $show(idSequence ));$skip(278); 

   def createUser(user: User): User = {
      val newId = idSequence.incrementAndGet()
      val createdUser = user.copy(id = Option(newId))
      users.put(newId, createdUser)
      createdUser
    };System.out.println("""createUser: (user: repositories.user.UserActions.User)repositories.user.UserActions.User""");$skip(161); 

    def updateUser(user: User): Boolean = {
      if (users.contains(user.id.get)) {
        users.put(user.id.get, user)
        true
      } else false
    };System.out.println("""updateUser: (user: repositories.user.UserActions.User)Boolean""");$skip(75); 

    def tryFindById(id: Long): Option[User] = {
      users.get(id)
    };System.out.println("""tryFindById: (id: Long)Option[repositories.user.UserActions.User]""");$skip(57); 

    def delete(id: Long) {
      users.remove(id)
    };System.out.println("""delete: (id: Long)Unit""");$skip(48); val res$0 = 

createUser(User(Option.empty, "abc@test.com"));System.out.println("""res0: repositories.user.UserActions.User = """ + $show(res$0));$skip(43); val res$1 = 

updateUser(User(Some(1), "foo@test.com"));System.out.println("""res1: Boolean = """ + $show(res$1));$skip(16); val res$2 = 

tryFindById(1);System.out.println("""res2: Option[repositories.user.UserActions.User] = """ + $show(res$2))}
}
