package domain.user

case class User(val id: Long,
                val email: String,
                val address: Option[Address])
                
case class Address(val firstLine: String, val secondLine: String,
    val city: String, val zip: String)
