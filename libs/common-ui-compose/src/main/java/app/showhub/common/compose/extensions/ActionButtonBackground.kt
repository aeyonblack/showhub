package app.showhub.common.compose.extensions

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

fun Modifier.actionButtonBackground(
    enabled: Boolean = false,
    @FloatRange alpha: Float = 0.3f,
    shape: Shape = RoundedCornerShape(6.dp),
    color: Color = Color.White
): Modifier = composed {
    if (enabled) {
        Modifier.background(
            color = color.copy(alpha),
            shape = shape
        )
    } else {
        Modifier.border(
            width = 1.dp,
            color = color.copy(alpha),
            shape = shape
        )
    }
}