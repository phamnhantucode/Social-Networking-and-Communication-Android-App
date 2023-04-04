package com.phamnhantucode.composeclonemessengerclient.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = darkColor2,
    primaryVariant = darkColor3,
    secondary = darkColor4,
    background = darkBackground,
    onBackground = Color.White,
    onSurface = Color(0xFF83878A)
)

private val LightColorPalette = lightColors(
    primary = lightColor4,
    primaryVariant = lightColor3,
    secondary = lightColor2,
    secondaryVariant = lightColor1,
    background = lightBackground,
    onSurface = Color.White,
    onBackground = Color(0xFF83878A)


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ComposeCloneMessengerClientTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}