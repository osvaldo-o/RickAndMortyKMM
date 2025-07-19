package osvaldo.oso.domain.repository

import osvaldo.oso.core.model.Error
import osvaldo.oso.core.model.Result
import osvaldo.oso.data.remote.model.CharacterApi


interface CharacterRepository {

    suspend fun getCharacters(): Result<List<CharacterApi>, Error>

    suspend fun getCharacterById(id: Int): Result<CharacterApi, Error>

}