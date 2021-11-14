package app.showhub.common.compose.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.TmdbImageEntity

@Composable
fun PosterCard(
    show: ShowEntity,
    modifier: Modifier = Modifier,
    poster: TmdbImageEntity? = null
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
    ) {
        Box(
            modifier = Modifier
        ) {
            Text(
                text = show.title ?: "No title",
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterStart)
                    .alpha(0.7f)
            )
            if (poster != null) {
                val image = loadPicture(poster.path).value
                image?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.matchParentSize()
                    )
                }
            } else {
                Log.d("posterCard", "poster is null")
            }
        }
    }
}