package osvaldo.oso

import androidx.compose.ui.window.ComposeUIViewController
import osvaldo.oso.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { App() }