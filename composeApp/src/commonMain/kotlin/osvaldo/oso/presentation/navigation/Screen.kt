package osvaldo.oso.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object CharactersScreen

@Serializable
object CharactersFavoriteScreen

@Serializable
data class CharacterDetailScreen(val isFavorite: Boolean)