package osvaldo.oso.presentation.viewmodel.home

import osvaldo.oso.domain.model.Character

data class HomeUiState(
    val characters: List<Character> = emptyList(),
    val isLoading: Boolean = false,
    val messageError: String = ""
)
