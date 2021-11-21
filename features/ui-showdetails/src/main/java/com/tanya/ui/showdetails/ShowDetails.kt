package com.tanya.ui.showdetails

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.showhub.common.compose.components.drawForegroundGradientScrim
import app.showhub.common.compose.components.loadPicture
import app.showhub.common.compose.extensions.copy
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
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
                                // If the first visible item is > 0, we want to show the app bar background
                                firstVisibleItem.index > 0 -> true
                                // If the first item is visible, only show the app bar background once the only
                                // remaining part of the item is <= the app bar
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
                backdrop = backdrop,
                showTitle = show.title ?: "",
                listState = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 10)
                    .clipToBounds()
            )
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
private fun BackdropImage(
    backdrop: ShowImagesEntity?,
    showTitle: String,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Box {
            if (backdrop != null) {
                val image = loadPicture(url = backdrop.path).value
                image?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .drawForegroundGradientScrim(
                                color = Color.Black.copy(1f),
                            )
                    )
                }
            }

            val textStyle = MaterialTheme.typography.h4

            Text(
                text = showTitle,
                style = textStyle.copy(
                    color = Color.White,
                ),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
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
        animationSpec = tween(1000)
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
        backgroundColor = backgroundColor,
        elevation = 0.dp,
        modifier = modifier
    )
}