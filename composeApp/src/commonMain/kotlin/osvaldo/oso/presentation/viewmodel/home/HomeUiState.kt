package osvaldo.oso.presentation.viewmodel.home

import osvaldo.oso.domain.model.Character

data class HomeUiState(
    val characters: List<Character> = emptyList(),
    val character: Character? = null,
    val isLoading: Boolean = false,
    val messageError: String = ""
)
