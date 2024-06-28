package com.example.todoya.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


val DarkColorPalette = darkColorScheme(
    primary = DarkColorPrimary,
    secondary = DarkColorSecondary,

    tertiary = Blue,
    error = Red,
    scrim = Green,
    outlineVariant = Grey,

    surface = DarkColorElevated,
    onPrimary = DarkIconColor,
    onSecondary = White,
    onSurface = White
)

val LightColorPalette = lightColorScheme(
    primary = LightColorPrimary,
    secondary = LightColorSecondary,

    tertiary = Blue,
    error = Red,
    scrim = Green,
    outlineVariant = Grey,

    surface = LightColorElevated,
    onPrimary = LightIconColor,
    onSecondary = Black,
    onSurface = Black
)



@Composable
fun TodoYaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}