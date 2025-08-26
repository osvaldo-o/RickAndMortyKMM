package osvaldo.oso.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import osvaldo.oso.presentation.theme.MortyColor
import osvaldo.oso.presentation.theme.PortalColor

enum class NavBarDestination(
    val label: String,
    val icon: ImageVector
) {
    CHARACTERS(label = "Characters", icon = Icons.Default.Group),
    FAVORITE(label = "Favorite", icon = Icons.Default.StarRate)
}

@Composable
fun NavBar(
    navController: NavController,
    selectDestination: Int,
    setDestination: (Int) -> Unit
) {

    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        containerColor = PortalColor
    ) {
        NavBarDestination.entries.forEachIndexed { index, destination ->
            NavigationBarItem(
                selected = selectDestination == index,
                onClick = {
                    when (destination) {
                        NavBarDestination.CHARACTERS -> navController.navigate(CharactersScreen)
                        NavBarDestination.FAVORITE -> navController.navigate(CharactersFavoriteScreen)
                    }
                    setDestination(index)
                },
                icon = { Icon(destination.icon, null) },
                label = { Text(destination.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MortyColor,
                    selectedTextColor = MortyColor,
                    indicatorColor = MortyColor.copy(alpha = 0.3f),
                    unselectedIconColor = MortyColor,
                    unselectedTextColor = MortyColor
                )
            )
        }
    }
}