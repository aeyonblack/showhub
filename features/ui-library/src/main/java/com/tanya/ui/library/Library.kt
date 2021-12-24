package com.tanya.ui.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle
import com.tanya.data.results.FollowedShowEntryWithShow

@Composable
fun Library(
    openShowDetails: (showId: Long) -> Unit
) {
    Library(
        viewModel = hiltViewModel(),
        openShowDetails = openShowDetails
    )
}

@Composable
internal fun Library(
    viewModel: LibraryViewModel,
    openShowDetails: (showId: Long) -> Unit
) {
    val viewState by rememberFlowWithLifeCycle(flow = viewModel.state)
        .collectAsState(initial = LibraryViewState.Empty)
    Library(
        state = viewState,
        list = rememberFlowWithLifeCycle(flow = viewModel.pagedList).collectAsLazyPagingItems()
    ) {

    }
}

@Composable
internal fun Library(
    state: LibraryViewState,
    list: LazyPagingItems<FollowedShowEntryWithShow>,
    dispatcher: (LibraryAction) -> Unit
) {

}