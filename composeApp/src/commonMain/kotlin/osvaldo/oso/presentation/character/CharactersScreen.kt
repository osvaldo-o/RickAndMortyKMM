package osvaldo.oso.presentation.character

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.flow.distinctUntilChanged
import osvaldo.oso.domain.model.Character
import osvaldo.oso.domain.model.Status
import osvaldo.oso.presentation.component.GlassCard
import osvaldo.oso.presentation.component.LoadingComponent

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CharactersScreen(
    characters: List<Character>,
    onCharacterClick: (Character) -> Unit,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    getCharacters: () -> Unit,
    isPageLimit: Boolean,
    isLoading: Boolean
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(lazyListState.canScrollForward) {
        snapshotFlow { lazyListState.canScrollForward }
            .distinctUntilChanged()
            .collect { canScrollForward ->
                if (!canScrollForward && !isPageLimit && !isLoading) {
                    getCharacters()
                }
            }
    }

    Scaffold { paddingValues ->
        BackgroundScreen {
            LazyColumn(
                state = lazyListState,
                modifier = modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(characters) { character ->
                    CharacterItem(
                        character = character,
                        onClick = { onCharacterClick(character) },
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }
                if (characters.isNotEmpty() && isLoading) {
                    item {
                        LoadingComponent(
                            isLoading,
                            modifierWrapper = Modifier.fillMaxSize().height(48.dp),
                            modifierContent = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun CharacterItem(
    character: Character,
    onClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            onClick = onClick,
            blurRadius = 32.dp,
            cornerRadius = 18.dp,
            backgroundColor = Color(0x4f584f).copy(alpha = 0.6f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                horizontalArrangement = Arrangement.Center
            ) {
                CharacterImage(
                    url = character.urlImage,
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxHeight()
                        .sharedElement(
                            rememberSharedContentState(key = "imagen-${character.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )
                CharacterData(
                    character = character,
                    modifier = Modifier
                        .weight(0.6f)
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
        }
    }
}

@Composable
private fun CharacterImage(
    url: String,
    modifier: Modifier
) {
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun CharacterData(
    character: Character,
    modifier: Modifier
) {
    val style = MaterialTheme.typography
    Column(modifier) {
        Text(
            character.name,
            style = style.titleMedium,
            color = Color.White
        )
        StatusAndSpecie(character.status, character.species)
        Spacer(Modifier.height(12.dp))
        InfoData("Last known location:", character.locationName)
        Spacer(Modifier.height(12.dp))
        InfoData("First seen in:", character.originName)
    }
}

@Composable
private fun InfoData(
    data: String,
    title: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth().wrapContentHeight()) {
        Text(data, style = MaterialTheme.typography.bodyMedium.copy(color = Color.White))
        Text(title, style = MaterialTheme.typography.titleSmall.copy(color = Color.White), maxLines = 1)
    }
}


@Composable
private fun StatusAndSpecie(
    status: Status,
    specie: String
) {
    val colorStatus = if (status == Status.ALIVE) Color.Green else Color.Red
    val statusTitle = status.name
        .lowercase()
        .split(" ")
        .joinToString(" ") { palabra ->
            palabra.replaceFirstChar { it.uppercase() }
        }
    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(colorStatus)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "$statusTitle - $specie",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun BackgroundScreen(
    modifierImage: Modifier = Modifier,
    urlImage: String = "https://s0.smartresize.com/wallpaper/776/686/HD-wallpaper-rick-y-morty-portal-phone-rick-and-morty-thumbnail.jpg",
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = urlImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifierImage.fillMaxSize()
        )
        content()
    }
}