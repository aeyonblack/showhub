package app.showhub.common.compose.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tanya.common.ui.resources.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandingCard(
    title: String,
    description: String,
    icon: Int,
    modifier: Modifier = Modifier,
    episodeNumber: Int? = null,
    date: String,
    shape: Shape = RoundedCornerShape(0.dp),
    expanded: Boolean = false,
) {
    Surface(
        modifier = Modifier
            .animateContentSize(
                tween(
                    durationMillis = 750,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = shape,
    ) {
        Column(modifier) {
            Row {
                Column(modifier = Modifier.fillMaxWidth(0.9f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = date,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
                Text(
                    text = stringResource(R.string.episode_number, episodeNumber ?: -1),
                    style = MaterialTheme.typography.subtitle2,
                )
            }
            if (expanded) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.body2,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}