package osvaldo.oso

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import osvaldo.oso.presentation.character.CharacterScreen
import osvaldo.oso.presentation.character.CharactersScreen
import osvaldo.oso.presentation.component.LoadingComponent
import osvaldo.oso.presentation.navigation.CharacterDetailScreen
import osvaldo.oso.presentation.navigation.CharactersScreen
import osvaldo.oso.presentation.viewmodel.home.HomeViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {

        val navController = rememberNavController()
        val viewModel: HomeViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        SharedTransitionLayout {
            NavHost(
                navController = navController,
                startDestination = CharactersScreen
            ) {
                composable<CharactersScreen> {
                    CharactersScreen(
                        characters = uiState.characters,
                        onCharacterClick = {
                            viewModel.setCharacter(it)
                            navController.navigate(CharacterDetailScreen)
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        getCharacters = { viewModel.getCharacterByPage() },
                        isPageLimit = uiState.isPageLimit,
                        isLoading = uiState.isLoading
                    )
                    if (uiState.characters.isEmpty())
                        LoadingComponent(
                            isLoading = uiState.isLoading,
                            modifierWrapper = Modifier.fillMaxSize(),
                            modifierContent = Modifier.size(38.dp)
                        )
                }

                composable<CharacterDetailScreen> {
                    uiState.character?.let {
                        CharacterScreen(
                            character = it,
                            onBackClick = {
                                navController.popBackStack()
                            },
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@composable
                        )
                    }
                }
            }
        }
    }
}