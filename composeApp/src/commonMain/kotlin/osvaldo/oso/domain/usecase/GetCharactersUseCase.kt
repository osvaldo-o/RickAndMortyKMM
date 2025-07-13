package osvaldo.oso.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import osvaldo.oso.core.model.Error
import osvaldo.oso.core.model.ResultState
import osvaldo.oso.domain.mapper.toCharacter
import osvaldo.oso.domain.model.Character
import osvaldo.oso.domain.repository.CharacterRepository

class GetCharactersUseCase constructor(
    private val repository: CharacterRepository
) {
    fun invoke(): Flow<ResultState<List<Character>, Error>> = flow {
        emit(ResultState.Loading())
        val response = repository.getCharacters()
        response.error?.let { error ->
            emit(ResultState.Failed(error))
        }
        emit(ResultState.Success(response.data?.map { characterApi ->
            characterApi.toCharacter()
        }))
    }
}