package osvaldo.oso.presentation.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import osvaldo.oso.core.model.Error
import osvaldo.oso.core.model.RemoteErrorWithCode
import osvaldo.oso.core.model.ResultState
import osvaldo.oso.domain.model.Character
import osvaldo.oso.domain.usecase.GetCharacterUseCase
import osvaldo.oso.domain.usecase.GetCharactersUseCase

class HomeViewModel constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getCharacterByIdUseCase: GetCharacterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getCharacters()
    }

    private fun getCharacters() {
        getCharactersUseCase.invoke().onEach { resultState ->
            when(resultState) {
                is ResultState.Failed<*> -> {
                }
                is ResultState.Loading<*> -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is ResultState.Success<List<Character>> -> {
                    _uiState.update { it.copy(isLoading = false) }
                    resultState.data?.let { characters ->
                        _uiState.update { it.copy(characters = characters) }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setCharacter(character: Character?) {
        _uiState.update { it.copy(character = character) }
    }

    fun getCharacterByPage() {
        if (uiState.value.currentPage+1 > 42) {
            _uiState.update { it.copy(isPageLimit = true) }
            return
        }
        getCharactersUseCase.invoke(uiState.value.currentPage+1).onEach { resultState ->
            when(resultState) {
                is ResultState.Failed<*> -> {}
                is ResultState.Loading<*> -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is ResultState.Success<*> -> {
                    delay(5000)
                    _uiState.update { it.copy(isLoading = false) }
                    resultState.data?.let { characters ->
                        _uiState.update { it.copy(
                            characters = (it.characters + characters).distinct(),
                            currentPage = it.currentPage + 1
                        ) }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getCharacterById(id: Int) {
        getCharacterByIdUseCase.invoke(id).onEach { resultState ->
            when(resultState) {
                is ResultState.Failed<*> -> {}
                is ResultState.Loading<*> -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is ResultState.Success<*> -> {
                    _uiState.update { it.copy(isLoading = false, character = resultState.data) }
                }
            }
        }.launchIn(viewModelScope)
    }

}