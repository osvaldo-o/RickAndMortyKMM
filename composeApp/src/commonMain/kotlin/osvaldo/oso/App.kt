package osvaldo.oso

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import osvaldo.oso.presentation.character.CharacterScreen
import osvaldo.oso.presentation.character.CharactersScreen
import osvaldo.oso.presentation.component.LoadingComponent
import osvaldo.oso.presentation.navigation.CharacterDetailScreen
import osvaldo.oso.presentation.navigation.CharactersFavoriteScreen
import osvaldo.oso.presentation.navigation.CharactersScreen
import osvaldo.oso.presentation.navigation.NavBar
import osvaldo.oso.presentation.navigation.NavBarDestination
import osvaldo.oso.presentation.viewmodel.home.HomeViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {

        val navController = rememberNavController()
        val viewModel: HomeViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        var isVisibilityNavBar by rememberSaveable{ mutableStateOf(true) }
        val snackBarHostState = remember { SnackbarHostState() }
        var selectDestination by rememberSaveable{ mutableStateOf(NavBarDestination.CHARACTERS.ordinal) }

        LaunchedEffect(uiState.message) {
            if (uiState.message.isEmpty()) return@LaunchedEffect
            snackBarHostState.showSnackbar(uiState.message)
        }

        Scaffold(
            bottomBar = {
                if (isVisibilityNavBar)
                    NavBar(navController, selectDestination, { selectDestination = it })
            },
            snackbarHost = { SnackbarHost(snackBarHostState) }
        ) { paddingValues ->
            SharedTransitionLayout {
                NavHost(
                    navController = navController,
                    startDestination = CharactersScreen,
                    modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())
                ) {

                    composable<CharactersScreen> {
                        CharactersScreen(
                            characters = uiState.characters,
                            onCharacterClick = {
                                viewModel.setCharacter(it)
                                isVisibilityNavBar = false
                                navController.navigate(CharacterDetailScreen(false))
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

                    composable<CharactersFavoriteScreen> {
                        val favorite by viewModel.charactersFavorite.collectAsStateWithLifecycle()
                        CharactersScreen(
                            characters = favorite,
                            onCharacterClick = {
                                viewModel.setCharacter(it)
                                isVisibilityNavBar = false
                                navController.navigate(CharacterDetailScreen(true))
                            },
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@composable,
                            getCharacters = {},
                            isPageLimit = false,
                            isLoading = false
                        )
                    }

                    composable<CharacterDetailScreen> { backStackEntry ->

                        val args = backStackEntry.toRoute<CharacterDetailScreen>()
                        val isFavorite = args.isFavorite

                        uiState.character?.let {
                            CharacterScreen(
                                character = it,
                                onBackClick = {
                                    isVisibilityNavBar = true
                                    navController.popBackStack()
                                },
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@composable,
                                actions = {
                                    if (isFavorite) {
                                        IconButton({
                                            isVisibilityNavBar = true
                                            navController.popBackStack()
                                            viewModel.deleteCharacterFavorite()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Rounded.Delete,
                                                contentDescription = "delete favorite",
                                                tint = MaterialTheme.colorScheme.surface
                                            )
                                        }
                                    } else {
                                        IconButton({ viewModel.saveCharacterFavorite() }) {
                                            Icon(
                                                imageVector = Icons.Rounded.StarRate,
                                                contentDescription = "save favorite",
                                                tint = MaterialTheme.colorScheme.surface
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}