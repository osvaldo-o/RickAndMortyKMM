package osvaldo.oso

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import osvaldo.oso.presentation.character.CharacterScreen
import osvaldo.oso.presentation.viewmodel.home.HomeViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel: HomeViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        CharacterScreen(uiState.characters)
    }
}