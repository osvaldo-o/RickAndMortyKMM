package osvaldo.oso.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    blurRadius: Dp = 16.dp,
    backgroundColor: Color = Color.White.copy(alpha = 0.15f),
    cornerRadius: Dp = 12.dp,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color.Transparent)
            .clickable { onClick.invoke() }
    ) {
        Box(
            modifier = Modifier.matchParentSize()
                .clip(RoundedCornerShape(cornerRadius))
                .background(backgroundColor)
                .blur(radius = blurRadius)
        )
        Box(Modifier.matchParentSize()) {
            content()
        }
    }
}