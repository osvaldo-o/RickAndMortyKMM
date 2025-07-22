package osvaldo.oso.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LoadingComponent(
    isLoading: Boolean,
    modifierWrapper: Modifier = Modifier,
    modifierContent: Modifier = Modifier
) {
    if (!isLoading) return
    Column(
        modifier = modifierWrapper,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifierContent,
            color = Color.Green
        )
    }
}