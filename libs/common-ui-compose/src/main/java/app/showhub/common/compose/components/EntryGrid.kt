package app.showhub.common.compose.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import app.showhub.common.compose.extensions.bodyWidth
import app.showhub.common.compose.extensions.itemsInGrid
import app.showhub.common.compose.utils.Layout
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.tanya.common.ui.resources.R
import com.tanya.data.Entry
import com.tanya.data.results.EntryWithShow

@Composable
fun <E: Entry> EntryGrid(
    lazyPagingItems: LazyPagingItems<out EntryWithShow<E>>,
    title: String,
    navigateUp: () -> Unit,
    openShowDetails: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            EntryGridAppBar(
                title = title,
                onNavigateUp = navigateUp,
                refreshing = lazyPagingItems.loadState.refresh == LoadState.Loading,
                onRefreshActionClick = { lazyPagingItems.refresh() },
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        val columns = Layout.columns
        val bodyMargin = Layout.bodyMargin
        val gutter = Layout.gutter

        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.bodyWidth().fillMaxHeight(),
        ) {
            itemsInGrid(
                lazyPagingItems = lazyPagingItems,
                columns = columns / 2,
                contentPadding = PaddingValues(horizontal = bodyMargin, vertical = gutter),
                verticalItemPadding = gutter,
                horizontalItemPadding = gutter,
            ) { entry ->
                val mod = Modifier
                    .aspectRatio(2 / 3f)
                    .fillMaxWidth()
                if (entry != null) {
                    PosterCard(
                        showTitle = entry.show.title,
                        posterPath = entry.poster?.path,
                        onClick = { openShowDetails(entry.show.id) },
                        modifier = mod
                    )
                } else {
                    // TODO: Add shimmer animation
                }
            }

            if (lazyPagingItems.loadState.append == LoadState.Loading) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

@Composable
private fun EntryGridAppBar(
    title: String,
    refreshing: Boolean,
    onNavigateUp: () -> Unit,
    onRefreshActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { onNavigateUp() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null
                )
            }
        },
        backgroundColor = Color.Black,
        elevation = 0.dp,
        contentColor = MaterialTheme.colors.onSurface,
        contentPadding = rememberInsetsPaddingValues(LocalWindowInsets.current.statusBars),
        modifier = modifier,
        title = { Text(text = title) },
        actions = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Crossfade(
                    targetState = refreshing,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) { isRefreshing ->
                    if (!isRefreshing) {
                        RefreshButton(onClick = onRefreshActionClick)
                    }
                }
            }
        },
    )
}