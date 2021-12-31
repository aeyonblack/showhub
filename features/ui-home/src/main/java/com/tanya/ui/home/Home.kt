package com.tanya.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.showhub.common.compose.components.AppBar
import app.showhub.common.compose.components.FollowedShowItem
import app.showhub.common.compose.components.PosterCard
import app.showhub.common.compose.extensions.itemsInGridIndexed
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle
import com.tanya.data.entities.ShowEntity
import com.tanya.data.results.EntryWithShow
import com.tanya.data.results.FollowedShowEntryWithShow

@Composable
fun Home(
    openShowDetails: (showId: Long, seasonId: Long?, episodeId: Long?) -> Unit,
    openPopularShows: () -> Unit,
    openTrendingShows: () -> Unit
) {
    Home(
        viewModel = hiltViewModel(),
        openShowDetails = openShowDetails,
        openPopularShows = openPopularShows,
        openTrendingShows = openTrendingShows
    )
}

@Composable
internal fun Home(
    viewModel: HomeViewModel,
    openPopularShows: () -> Unit,
    openTrendingShows: () -> Unit,
    openShowDetails: (showId: Long, seasonId: Long?, episodeId: Long?) -> Unit
) {
    val viewState by rememberFlowWithLifeCycle(flow = viewModel.state)
        .collectAsState(initial = HomeViewState.EMPTY)
    Home(
        state = viewState,
        followedShows = rememberFlowWithLifeCycle(flow = viewModel.pagedFollowedShows)
            .collectAsLazyPagingItems(),
        openShowDetails = openShowDetails,
        openPopularShows = openPopularShows,
        openTrendingShows = openTrendingShows
    )
}

@Composable
internal fun Home(
    state: HomeViewState,
    openTrendingShows: () -> Unit,
    openPopularShows: () -> Unit,
    followedShows: LazyPagingItems<FollowedShowEntryWithShow>,
    openShowDetails: (showId: Long, seasonId: Long?, episodeId: Long?) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        item { AppBar(title = "Hey there!") }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        /*following shows*/
        if (followedShows.itemCount > 0) {
            itemsInGridIndexed(
                lazyPagingItems = followedShows,
                columns = 2,
                horizontalItemPadding = 8.dp,
                verticalItemPadding = 8.dp,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) { show, index ->
                if (show != null && index < 4) {
                    FollowedShowItem(
                        show = show.show,
                        poster = show.poster,
                        showInfo = false,
                        posterShape = RoundedCornerShape(0.dp),
                        shape = RoundedCornerShape(8.dp),
                        posterWidth = 0.25f,
                        titleFontWeight = Bold,
                        backgroundColor = Color.DarkGray.copy(0.5f),
                        onClick = { openShowDetails(show.show.id, null, null) }
                    )
                }
            }
        }

        /*trending items*/
        item {
            CarouselWithHeader(
                items = state.trendingItems,
                title = "People are watching",
                refreshing = state.trendingRefreshing,
                onMoreClicked = openTrendingShows,
                onItemClick = {
                    openShowDetails(it.id, null, null)
                }
            )
        }

        /*popular items*/
        item {
            CarouselWithHeader(
                items = state.popularItems,
                title = "Top rated",
                refreshing = state.popularRefreshing,
                onMoreClicked = openPopularShows,
                onItemClick = {
                    openShowDetails(it.id, null, null)
                }
            )
        }

        item { Spacer(modifier = Modifier.height(128.dp)) }
    }
}

@Composable
private fun <T: EntryWithShow<*>> CarouselWithHeader(
    items: List<T>,
    title: String,
    refreshing: Boolean,
    onMoreClicked: () -> Unit,
    onItemClick: (ShowEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        if (refreshing || items.isNotEmpty()) {
            Header(
                title = title,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = { onMoreClicked() },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(text = "All")
                }
            }

        }
        if (items.isNotEmpty()) {
            EntryShowCarousel(
                items = items,
                onItemClick = onItemClick,
                modifier = Modifier
                    .height(192.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun Header(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit = {}
) {
    Row(modifier) {
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        content()
    }
}

@Composable
private fun <T: EntryWithShow<*>> EntryShowCarousel(
    items: List<T>,
    onItemClick: (ShowEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    
    LazyRow(
        state = listState,
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items) {
            PosterCard(
                showTitle = it.show.title,
                posterPath = it.poster?.path,
                onClick = { onItemClick(it.show) },
                modifier = Modifier
                    .fillParentMaxHeight()
                    .aspectRatio(2 / 3f)
            )
        }
    }
}