package osvaldo.oso.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseApi(
    val info: Info,
    val results: List<CharacterApi>
)

@Serializable
data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)
