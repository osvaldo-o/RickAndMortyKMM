package osvaldo.oso.presentation.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import osvaldo.oso.core.model.ResultState
import osvaldo.oso.domain.model.Character
import osvaldo.oso.domain.repository.CharacterRepository
import osvaldo.oso.domain.usecase.DeleteCharacterUseCase
import osvaldo.oso.domain.usecase.GetCharactersUseCase
import osvaldo.oso.domain.usecase.SaveCharacterUseCase

class HomeViewModel (
    private val getCharactersUseCase: GetCharactersUseCase,
    private val saveCharacterUseCase: SaveCharacterUseCase,
    private val deleteCharacterUseCase: DeleteCharacterUseCase,
    repository: CharacterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
    val charactersFavorite = repository.getCharacterFavorite()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        getCharacters()
    }

    private fun getCharacters() {
        getCharactersUseCase.invoke().onEach { resultState ->
            when(resultState) {
                is ResultState.Failed -> {
                }
                is ResultState.Loading -> {
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
                is ResultState.Failed -> {}
                is ResultState.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is ResultState.Success -> {
                    delay(2000)
                    _uiState.update { it.copy(isLoading = false) }
                    resultState.data?.let { characters ->
                        _uiState.update { it.copy(
                            characters = (it.characters + characters).distinct(),
                            currentPage = it.currentPage + 1
                        )}
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun saveCharacterFavorite() {
        _uiState.value.character?.let { character ->
            saveCharacterUseCase.invoke(character).onEach { resultState ->
                when (resultState) {
                    is ResultState.Loading<*> -> {}
                    is ResultState.Failed<*> -> {}
                    is ResultState.Success<*> -> {
                        _uiState.update { it.copy(message = "save ${character.name} in favorite") }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun deleteCharacterFavorite() {
        _uiState.value.character?.let { character ->
            deleteCharacterUseCase.invoke(character.id).onEach { resultState ->
                when(resultState) {
                    is ResultState.Failed<*> -> {}
                    is ResultState.Loading<*> -> {}
                    is ResultState.Success<*> -> {
                        _uiState.update { it.copy(message = "delete ${character.name} in favorite") }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

}