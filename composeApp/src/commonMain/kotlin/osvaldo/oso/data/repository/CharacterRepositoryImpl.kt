package osvaldo.oso.data.repository

import osvaldo.oso.core.model.Error
import osvaldo.oso.core.model.Result
import osvaldo.oso.data.remote.ktorClient.KtorApiClient
import osvaldo.oso.data.remote.model.CharacterApi
import osvaldo.oso.data.remote.model.ResponseApi
import osvaldo.oso.domain.repository.CharacterRepository

class CharacterRepositoryImpl (
    private val ktorApiClient: KtorApiClient
) : CharacterRepository {

    override suspend fun getCharacters(): Result<List<CharacterApi>, Error> {
        val response = ktorApiClient.get<ResponseApi>("character")
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

}