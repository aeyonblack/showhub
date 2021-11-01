package app.showhub.common.compose.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

val gray600 = Color(0xFF979797)
val gray700 = Color(0xFF818181)
val gray800 = Color(0xFF606060)
val gray900 = Color(0xFF3C3C3C)

val green300 = Color(0xFF75FF58)
val green400 = Color(0xFF2FFF00)
val green500 = Color(0xFF00FC00)

val yellow200 = Color(0xFFFFEB46)
val yellow400 = Color(0xFFFFC000)
val yellow500 = Color(0xFFFFDE03)

val pink200 = Color(0xFFFF7597)
val pink500 = Color(0xFFFF0266)
val pink600 = Color(0xFFD8004D)

val purple500 = Color(0xFFDE00B8)
val purple600 = Color(0xFFCF00B5)
val purple700 = Color(0xFFBA00B1)
val purple800 = Color(0xFFA700AC)
val purple900 = Color(0xFF8400A4)

/**
 * Return the fully opaque color that results from compositing onSurface atop surface with the
 * given [alpha]. Useful for situations where semi-transparent colors are undesirable.
 */
@Composable
fun Colors.compositedOnSurface(alpha: Float): Color {
    return onSurface.copy(alpha = alpha).compositeOver(surface)
}
