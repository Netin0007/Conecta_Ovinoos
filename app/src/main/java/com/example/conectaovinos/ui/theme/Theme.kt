package com.example.conectaovinos.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PetNaturaGreen,           // Cor principal para botões, app bars, etc.
    onPrimary = WhiteText,              // Cor do texto SOBRE a cor primária (IMPORTANTE para contraste!)
    secondary = PetNaturaPeach,         // Cor para botões de ação flutuantes, destaques.
    onSecondary = PetNaturaDarkText,    // Texto sobre a cor secundária.
    background = PetNaturaLightGray,    // Cor de fundo principal do app.
    onBackground = PetNaturaDarkText,   // Cor do texto principal sobre o fundo.
    surface = PetNaturaLightGray,       // Cor de superfície para Cards, menus.
    onSurface = PetNaturaDarkText,      // Cor do texto sobre superfícies.

)

private val DarkColorScheme = darkColorScheme(
    primary = PetNaturaLightGreen,
    onPrimary = PetNaturaDarkText,
    secondary = PetNaturaPeach,
    onSecondary = PetNaturaDarkText,
    background = PetNaturaDarkText,
    onBackground = WhiteText,
    surface = PetNaturaDarkText,
    onSurface = WhiteText
)

@Composable
fun ConectaOvinosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Este vem do arquivo Typography.kt
        content = content
    )
}