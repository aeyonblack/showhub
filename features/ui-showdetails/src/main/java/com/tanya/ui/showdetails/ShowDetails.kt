package com.tanya.ui.showdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle

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
    Column(modifier = Modifier.padding(16.dp)) {
        state.show.title?.let { Text(text = it) }
    }
}