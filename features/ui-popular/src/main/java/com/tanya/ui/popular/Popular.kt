package com.tanya.ui.popular

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import app.showhub.common.compose.components.EntryGrid
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle

@Composable
fun Popular(
    openShowsDetails: (showId: Long) -> Unit,
    navigateUp: () -> Unit
) {
    Popular(
        viewModel = hiltViewModel(),
        openShowsDetails = openShowsDetails,
        navigateUp = navigateUp
    )
}

@Composable
internal fun Popular(
    viewModel: PopularShowsViewModel,
    openShowsDetails: (showId: Long) -> Unit,
    navigateUp: () -> Unit
) {
    EntryGrid(
        lazyPagingItems = rememberFlowWithLifeCycle(flow = viewModel.pagedList).collectAsLazyPagingItems(),
        title = "Top rated",
        navigateUp = navigateUp,
        openShowDetails = openShowsDetails,
    )
}