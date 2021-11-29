package app.showhub.common.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun PosterCard(
    showTitle: String?,
    modifier: Modifier = Modifier,
    posterPath: String? = null,
    shape: Shape? = null,
    onClick: (() -> Unit)? = null
) {
    Card(
        shape = shape ?: MaterialTheme.shapes.medium,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
    ) {
        Box(
            modifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        ) {
            Text(
                text = showTitle ?: "No title",
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterStart)
                    .alpha(0.7f)
            )
            if (posterPath != null) {
                val image = loadPicture(posterPath).value
                image?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.matchParentSize()
                    )
                }
            }
        }
    }
}