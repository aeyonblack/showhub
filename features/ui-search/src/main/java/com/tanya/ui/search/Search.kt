package com.tanya.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle

@Composable
fun Search(
    openShowDetails: (showId: Long) -> Unit
) {
    Search(
        viewModel = hiltViewModel(),
        openShowDetails = openShowDetails
    )
}

@Composable
internal fun Search(
    viewModel: SearchViewModel,
    openShowDetails: (showId: Long) -> Unit
) {
    val viewState by rememberFlowWithLifeCycle(flow = viewModel.state)
        .collectAsState(initial = SearchViewState.Empty)
    Search(viewState = viewState) {
        when(it) {
            is SearchAction.OpenShowDetails -> openShowDetails(it.showId)
            else -> viewModel.submitAction(it)
        }
    }
}

@Composable
internal fun Search(
    viewState: SearchViewState,
    dispatcher: (SearchAction) -> Unit
) {

}

@Composable
private fun SearchBox() {
    Surface() {
        Column() {
            Text(
                text = "Search"
            )

        }
    }
}