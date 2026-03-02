package com.example.tutoclass.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = TutoGreen,
    secondary = TutoYellow,
    tertiary = TutoTextDark
)

private val LightColorScheme = lightColorScheme(
    primary = TutoGreen,
    secondary = TutoYellow,
    tertiary = TutoTextDark,
    background = TutoBgCanvas
)

@Composable
fun TutoClassTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}