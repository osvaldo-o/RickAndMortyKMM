package osvaldo.oso.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object CharactersScreen

@Serializable
data class CharacterDetailScreen(val characterId: Int)