package app.showhub.common.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.TmdbImageEntity

@Composable
fun PosterCard(
    show: ShowEntity,
    modifier: Modifier = Modifier,
    poster: TmdbImageEntity? = null
) {
    Card(modifier = modifier,
    backgroundColor = MaterialTheme.colors.surface,
    contentColor = MaterialTheme.colors.onSurface) {
        Box(
            modifier = Modifier
        ) {
            Text(
                text = show.title ?: "No title",
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterStart)
            )
            if (poster != null) {
                Image(
                    painter = rememberImagePainter(data = poster) {
                        crossfade(true)
                    },
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun PlaceholderPosterCard(
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Box {
            // TODO: display something better
        }
    }
}