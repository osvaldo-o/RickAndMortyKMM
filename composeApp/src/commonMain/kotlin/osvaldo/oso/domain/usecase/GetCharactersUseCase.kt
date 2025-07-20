package osvaldo.oso.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import osvaldo.oso.core.model.Error
import osvaldo.oso.core.model.RemoteErrorWithCode
import osvaldo.oso.core.model.ResultState
import osvaldo.oso.domain.mapper.toCharacter
import osvaldo.oso.domain.model.Character
import osvaldo.oso.domain.repository.CharacterRepository

class GetCharactersUseCase (
    private val repository: CharacterRepository
) {

    fun invoke(page: Int = 1): Flow<ResultState<List<Character>, Error>> = flow {
        emit(ResultState.Loading())
        if (page > 42) {
            emit(ResultState.Failed(RemoteErrorWithCode(error = Error.Remote.CLIENT_ERROR, code = 400)))
            return@flow
        }
        val response = repository.getCharacters(page)
        response.error?.let { error ->
            emit(ResultState.Failed(error))
        }
        emit(ResultState.Success(response.data?.map { characterApi ->
            characterApi.toCharacter()
        }))
    }
}