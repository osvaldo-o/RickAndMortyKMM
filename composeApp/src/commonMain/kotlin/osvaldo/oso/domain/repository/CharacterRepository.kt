package osvaldo.oso.domain.repository

import kotlinx.coroutines.flow.Flow
import osvaldo.oso.core.model.Error
import osvaldo.oso.core.model.Result
import osvaldo.oso.data.remote.model.CharacterApi
import osvaldo.oso.domain.model.Character


interface CharacterRepository {

    suspend fun getCharacters(page: Int): Result<List<CharacterApi>, Error>

    suspend fun getCharacterById(id: Int): Result<CharacterApi, Error>

    suspend fun saveCharacter(character: Character)

    fun getCharacterFavorite(): Flow<List<Character>>

    suspend fun deleteCharacter(id: Int)

}