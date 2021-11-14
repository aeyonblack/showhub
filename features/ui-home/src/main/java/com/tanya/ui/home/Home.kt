package com.tanya.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.showhub.common.compose.components.AppBar
import app.showhub.common.compose.components.PosterCard
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle
import com.tanya.data.results.EntryWithShow

@Composable
fun Home() {
    Home(
        viewModel = hiltViewModel()
    )
}

@Composable
internal fun Home(
    viewModel: HomeViewModel
) {
    val viewState by rememberFlowWithLifeCycle(flow = viewModel.state)
        .collectAsState(initial = HomeViewState.EMPTY)
    Home(
        state = viewState
    )
}

@Composable
internal fun Home(
    state: HomeViewState
) {
    Scaffold(
        topBar = { AppBar("Hello!")},
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            /*trending items*/
            item {
                CarouselWithHeader(
                    items = state.trendingItems,
                    title = "People are watching",
                    refreshing = state.trendingRefreshing
                )
            }
            /*popular items*/
            item {
                CarouselWithHeader(
                    items = state.popularItems,
                    title = "Top rated",
                    refreshing = state.popularRefreshing
                )
            }
        }
    }
}

@Composable
private fun <T: EntryWithShow<*>> CarouselWithHeader(
    items: List<T>,
    title: String,
    refreshing: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        if (refreshing || items.isNotEmpty()) {
            Header(
                title = title,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(text = "All")
                }
            }

        }
        if (items.isNotEmpty()) {
            EntryShowCarousel(
                items = items,
                modifier = Modifier
                    .height(192.dp)
                    .fillMaxWidth()
            )
        }
        else {
            Header(
                title = "Nothing to show!"
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
                show = it.show,
                poster = it.poster,
                modifier = Modifier
                    .fillParentMaxHeight()
                    .aspectRatio(2 / 3f)
            )
        }
    }
}