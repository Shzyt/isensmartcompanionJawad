package fr.isen.ghitri.isensmartcompanion.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFC30000), // Rouge ISEN
    onPrimary = Color.White,
    secondary = Color(0xFF6200EE), // Violet Material 3
    onSecondary = Color.White,
    background = Color(0xFFF2F2F7), // Gris clair
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)


private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF6D00),
    onPrimary = Color.Black,
    secondary = Color(0xFFBB86FC),
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)


@Composable
fun ThemeBonus(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

