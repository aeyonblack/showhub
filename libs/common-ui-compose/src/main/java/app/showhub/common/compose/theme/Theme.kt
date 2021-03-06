package app.showhub.common.compose.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val showhubDefault = darkColors(
    primary = pink500,
    primaryVariant = pink600,
    onPrimary = Color.White,
    secondary = green400,
    secondaryVariant = green500,
    onSecondary = Color.Black,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White
)

private val showhubSecondary = darkColors(
    primary = pink200,
    secondary = pink200,
    onPrimary = Color.Black,
    onSecondary = Color.Black
)

@Composable
fun ShowhubTheme(content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = false
    )
    val elevations = Elevations(card = 1.dp)
    CompositionLocalProvider(LocalElevations provides elevations) {
        MaterialTheme(
            colors = showhubDefault,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}

@Composable
fun ShowhubPinkTheme(content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = false
    )
    val elevations = Elevations(card = 1.dp)
    CompositionLocalProvider(LocalElevations provides elevations) {
        MaterialTheme(
            colors = showhubSecondary,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}