package domain.user

object UserLookup {
  val u = User(1, "abc@gmail.com", None)
  
  val listUsers = Map{u.id -> u,
      2 -> u.copy(2, "2@gmail.com"),
      3 -> u.copy(3, "3@gmail.com"),
      4 -> u.copy(4)}
  
}