package com.tanya.ui.showdetails

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.showhub.common.compose.components.ExpandingText
import app.showhub.common.compose.components.drawForegroundGradientScrim
import app.showhub.common.compose.components.loadPicture
import app.showhub.common.compose.extensions.copy
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.tanya.common.ui.resources.R
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity

@Composable
fun ShowDetails() {
    ShowDetails(
        viewModel = hiltViewModel()
    )
}

@Composable
internal fun ShowDetails(
    viewModel: ShowDetailsViewModel
) {
    val viewState by rememberFlowWithLifeCycle(flow = viewModel.state)
        .collectAsState(initial = ShowDetailsViewState.EMPTY)
    ShowDetails(
        state = viewState
    )
}

@Composable
internal fun ShowDetails(
    state: ShowDetailsViewState
) {
    val listState = rememberLazyListState()
    Scaffold(
        topBar = {
            var appBarHeight by remember { mutableStateOf(0) }
            val showAppBarBackground by remember {
                derivedStateOf {
                    val visibleItemsInfo = listState.layoutInfo.visibleItemsInfo
                    when {
                        visibleItemsInfo.isEmpty() -> false
                        appBarHeight <= 0 -> false
                        else -> {
                            val firstVisibleItem = visibleItemsInfo[0]
                            when {
                                firstVisibleItem.index > 0 -> true
                                else -> firstVisibleItem.size + firstVisibleItem.offset <= appBarHeight
                            }
                        }
                    }
                }
            }
            ShowDetailsAppBar(
                title = state.show.title,
                showBackground = showAppBarBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { appBarHeight = it.height }
            )
        }
    ) { contentPadding ->
        Surface {
            ShowDetailsContent(
                show = state.show,
                listState = listState,
                backdrop = state.backdropImage,
                contentPadding = contentPadding,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
internal fun ShowDetailsContent(
    show: ShowEntity,
    listState: LazyListState,
    backdrop: ShowImagesEntity?,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        contentPadding = contentPadding.copy(copyTop = false),
        modifier = modifier
    ) {
        item {
            BackdropImage(
                show = show,
                backdrop = backdrop,
                listState = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 10)
                    .clipToBounds()
            )
        }
        item {
            FollowShowButton(
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Text(text = "Overview")
        }
        item {
            show.summary?.let {
                ExpandingText(text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp))
            }
        }
        repeat(50) {
            item {
                Text(
                    text = "List Content",
                )
            }
        }
    }
}

@Composable
private fun Header(
    title: String
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

    }
}

@Composable
private fun BackdropImage(
    show: ShowEntity,
    backdrop: ShowImagesEntity?,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    var decay by remember { mutableStateOf(1f)}
    decay = 1/(listState.firstVisibleItemScrollOffset/80f)
    Surface(modifier = modifier) {
        Box {
            if (backdrop != null) {
                val image = loadPicture(url = backdrop.path).value
                image?.let { img ->
                    Image(
                        bitmap = img.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .drawForegroundGradientScrim(
                                color = Color.Black.copy(1f),
                                decay = minOf(1f, decay)
                            )
                    )
                }
            }
            BackdropContent(
                showTitle = show.title ?: "",
                rating = show.traktRating ?: 0f,
                votes = show.traktVotes ?: 0,
                decay = decay,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun FollowShowButton(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedButton(
            onClick = { /*TODO*/ },
            border = BorderStroke(1.dp, MaterialTheme.colors.onBackground),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Follow",
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
            )
        }
    }
}

@Composable
private fun ShowDetailsAppBar(
    title: String?,
    showBackground: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            showBackground -> Color.Black
            else -> Color.Transparent
        },
        animationSpec = tween()
    )
    
    TopAppBar(
        title = {
            Crossfade(
                targetState = showBackground && title != null,
                animationSpec = tween(1000)
            ) {
                if (it) Text(text = title!!)
            }
        },
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyBottom = false
        ),
        navigationIcon = {
            IconButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        },
        actions = {
            IconButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_refresh),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        },
        backgroundColor = backgroundColor,
        elevation = 0.dp,
        modifier = modifier
    )
}

@Composable
private fun BackdropContent(
    showTitle: String,
    rating: Float,
    votes: Int,
    decay: Float,
    modifier: Modifier = Modifier,
) {
    val titleStyle = MaterialTheme.typography.h4
    Column(modifier) {
        Text(
            text = showTitle,
            style = titleStyle.copy(
                color = Color.White,
            ),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .alpha(minOf(1f, decay))
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(R.string.trakt_rating_text, rating*10f, votes/1000f),
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(minOf(1f, decay))
                .align(Alignment.CenterHorizontally)
        )
        /*Text(
            text = show.certification ?: "",
            style = MaterialTheme.typography.body2,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.onSurface,
                    shape = RoundedCornerShape(4.dp),
                )
                .alpha(minOf(1f,decay))
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
                .align(Alignment.CenterHorizontally)
                .alpha(minOf(1f,decay))
        )*/
    }
}