package osvaldo.oso.presentation.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
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
                is ResultState.Failed<*> -> {}
                is ResultState.Loading<*> -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is ResultState.Success<*> -> {
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