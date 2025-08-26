package osvaldo.oso.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import osvaldo.oso.core.model.Error
import osvaldo.oso.core.model.ResultState
import osvaldo.oso.domain.repository.CharacterRepository

class DeleteCharacterUseCase(
    private val repository: CharacterRepository
) {

    fun invoke(id: Int): Flow<ResultState<Any, Error>> = flow {
        emit(ResultState.Loading())
        try {
            repository.deleteCharacter(id)
            emit(ResultState.Success(null))
        } catch (e: Exception) {
            emit(ResultState.Failed(Error.Local.UNKNOWN))
        }
    }
}