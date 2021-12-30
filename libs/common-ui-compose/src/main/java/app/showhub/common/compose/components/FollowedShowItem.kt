package app.showhub.common.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity

@Composable
fun FollowedShowItem(
    show: ShowEntity,
    poster: ShowImagesEntity?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showInfo: Boolean = true,
    shape: Shape? = null,
    posterShape: Shape? = null,
    backgroundColor: Color = Color.Transparent,
    posterWidth: Float = 0.2f,
    titleFontWeight: FontWeight? = null,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(contentPadding)
            .background(backgroundColor, shape ?: RoundedCornerShape(0.dp))
    ) {
        PosterCard(
            showTitle = show.title,
            posterPath = poster?.path,
            shape = posterShape,
            modifier = Modifier
                .fillMaxWidth(posterWidth)
                .aspectRatio(2 / 3f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = show.title ?: "",
                style = MaterialTheme.typography.subtitle2,
                fontWeight = titleFontWeight ?: FontWeight.W500,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.padding(end = 4.dp)
            )
            if (show.summary?.isNotEmpty() == true && showInfo) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = show.summary!!,
                        style = MaterialTheme.typography.caption,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                }
            }
        }
    }
}