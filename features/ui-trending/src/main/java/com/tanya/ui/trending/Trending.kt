package com.tanya.ui.trending

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import app.showhub.common.compose.components.EntryGrid
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle

@Composable
fun Trending(
    openShowDetails: (showId: Long) -> Unit,
    navigateUp: () -> Unit
) {
    Trending(
        viewModel = hiltViewModel(),
        openShowDetails = openShowDetails,
        navigateUp = navigateUp
    )
}

@Composable
internal fun Trending(
    viewModel: TrendingShowsViewModel,
    openShowDetails: (showId: Long) -> Unit,
    navigateUp: () -> Unit
) {
    EntryGrid(
        lazyPagingItems = rememberFlowWithLifeCycle(flow = viewModel.pagedList).collectAsLazyPagingItems(),
        title = "Trending",
        navigateUp = navigateUp,
        openShowDetails = openShowDetails
    )
}