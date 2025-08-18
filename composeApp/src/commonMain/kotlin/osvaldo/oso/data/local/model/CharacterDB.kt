package osvaldo.oso.data.local.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterDB(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val originName: String,
    val locationName: String,
    val urlImage: String
)