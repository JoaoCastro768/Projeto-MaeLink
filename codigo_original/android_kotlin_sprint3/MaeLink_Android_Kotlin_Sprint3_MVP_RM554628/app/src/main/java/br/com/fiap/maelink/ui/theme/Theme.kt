package br.com.fiap.maelink.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = MaeTeal,
    secondary = MaeTealDark,
    background = MaeBackground,
    surface = MaeBackground,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onBackground = MaeText,
    onSurface = MaeText
)

@Composable
fun MaeLinkTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}
