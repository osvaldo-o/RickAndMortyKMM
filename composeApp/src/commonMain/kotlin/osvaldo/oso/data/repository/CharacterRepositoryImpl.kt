package osvaldo.oso.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import osvaldo.oso.core.model.Error
import osvaldo.oso.core.model.Result
import osvaldo.oso.data.local.source.LocalDataSource
import osvaldo.oso.data.mapper.toCharacter
import osvaldo.oso.data.mapper.toCharacterDB
import osvaldo.oso.data.remote.ktorClient.KtorApiClient
import osvaldo.oso.data.remote.model.CharacterApi
import osvaldo.oso.data.remote.model.ResponseApi
import osvaldo.oso.domain.model.Character
import osvaldo.oso.domain.repository.CharacterRepository

class CharacterRepositoryImpl (
    private val ktorApiClient: KtorApiClient,
    private val localDataSource: LocalDataSource
) : CharacterRepository {

    override suspend fun getCharacters(page: Int): Result<List<CharacterApi>, Error> {
        val response = ktorApiClient.get<ResponseApi>(
            route = "character",
            parameters = mapOf("page" to page.toString())
        )
        response.error?.let { error ->
            return Result.Failed(error)
        }
        return Result.Success(response.data?.results)
    }

    override suspend fun getCharacterById(id: Int): Result<CharacterApi, Error> {
        val response = ktorApiClient.get<CharacterApi>("character/$id")
        response.error?.let { error ->
            return Result.Failed(error)
        }
        return Result.Success(response.data)
    }

    override suspend fun saveCharacter(character: Character) {
        localDataSource.insertCharacter(character.toCharacterDB())
    }

    override fun getCharacterFavorite(): Flow<List<Character>> {
        return localDataSource.getCharacters().map { characters -> characters.map { it.toCharacter() } }
    }

    override suspend fun deleteCharacter(id: Int) {
        localDataSource.deleteCharacter(id.toLong())
    }

}