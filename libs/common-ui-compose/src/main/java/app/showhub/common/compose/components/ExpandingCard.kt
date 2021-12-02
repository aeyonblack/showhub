package app.showhub.common.compose.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.showhub.common.compose.extensions.actionButtonBackground

@Composable
fun ExpandingCard(
    title: String,
    description: String,
    icon: Int,
    modifier: Modifier = Modifier,
    date: String? = null,
    shape: Shape = RoundedCornerShape(0.dp),
    watched: Boolean = false,
    onWatchClicked: () -> Unit
) {
    val expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = shape,
    ) {
        Column(modifier) {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                if (date != null) {
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
            if (expanded) {
                Text(
                    text = description
                )
                Text(
                    text = when {
                        watched -> "Remove watch"
                        else -> "Mark watched"
                    },
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onBackground.copy(1f),
                    modifier = Modifier
                        .clickable {
                            onWatchClicked()
                        }
                        .actionButtonBackground(
                            enabled = !watched,
                            alpha = 1f
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}