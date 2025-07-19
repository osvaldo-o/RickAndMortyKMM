package osvaldo.oso.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import osvaldo.oso.core.model.Error
import osvaldo.oso.core.model.ResultState
import osvaldo.oso.domain.mapper.toCharacter
import osvaldo.oso.domain.model.Character
import osvaldo.oso.domain.repository.CharacterRepository

class GetCharacterUseCase(
    private val repository: CharacterRepository
) {

    fun invoke(id: Int): Flow<ResultState<Character, Error>> = flow {
        emit(ResultState.Loading())
        val response = repository.getCharacterById(id)
        response.error?.let { error ->
            emit(ResultState.Failed(error))
        }
        emit(ResultState.Success(response.data?.toCharacter()))
    }
}