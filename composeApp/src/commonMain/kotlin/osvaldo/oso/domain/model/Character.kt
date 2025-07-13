package osvaldo.oso.domain.model

data class Character(
    val name: String,
    val status: Status,
    val species: String,
    val gender: String,
    val originName: String,
    val locationName: String,
    val urlImage: String
)

enum class Status {
    ALIVE,
    DEAD
}
