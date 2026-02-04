package com.example.conectaovinos.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val SertaoColorScheme = lightColorScheme(
    primary = TerraBarro,
    onPrimary = Color.White,
    secondary = SolNordeste,
    onSecondary = TextoPrincipal,
    tertiary = VerdeCaatinga,
    onTertiary = Color.White,
    background = CinzaAreia,
    onBackground = TextoPrincipal,
    surface = Color(0xFFF0EDE4),
    onSurface = TextoPrincipal,
    error = VermelhoBarro,
    onError = Color.White
)

@Composable
fun ConectaOvinosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SertaoColorScheme,
        typography = Typography,
        content = content
    )
}