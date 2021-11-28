package app.showhub.common.compose.components

import android.annotation.SuppressLint
import androidx.annotation.FloatRange
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.pow

/**
 * Draws a vertical gradient scrim in the foreground.
 *
 * @param color The color of the gradient scrim.
 * @param decay The exponential decay to apply to the gradient. Defaults to `3.0f` which is
 * a cubic decay.
 * @param numStops The number of color stops to draw in the gradient. Higher numbers result in
 * the higher visual quality at the cost of draw performance. Defaults to `16`.
 */
@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.drawForegroundGradientScrim(
    color: Color,
    decay: Float = 1f,
    numStops: Int = 32,
    @FloatRange(from = 0.0, to = 1.0) startY: Float = 0f,
    @FloatRange(from = 0.0, to = 500.0) endY: Float = 500f,
): Modifier = composed {
    val colors = remember(color, numStops, decay) {
        val baseAlpha = color.alpha
        List(numStops) { i ->
            val x = i * 1f / (numStops - 1)
            val opacity = x.pow(decay)
            color.copy(alpha = baseAlpha * opacity)
        }
    }

    drawWithContent {
        drawContent()
        drawRect(
            topLeft = Offset(x = 0f, y = startY * size.height),
            size = size.copy(height = (endY - startY) * size.height),
            brush = Brush.verticalGradient(colors = colors)
        )
    }
}