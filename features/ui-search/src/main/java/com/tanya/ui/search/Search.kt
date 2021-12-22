package com.tanya.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.showhub.common.compose.components.SearchTextField
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle
import com.google.accompanist.insets.statusBarsPadding

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
    Column {
        SearchBox(state = viewState, dispatcher = dispatcher)
    }
}

@Composable
private fun SearchBox(
    state: SearchViewState,
    dispatcher: (SearchAction) -> Unit
) {
    Surface(modifier = Modifier.statusBarsPadding()) {
        Column(modifier = Modifier.padding(16.dp)) {
            var searchQuery by remember {
                mutableStateOf(TextFieldValue(state.query))
            }
            Text(
                text = "Search",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            SearchTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    dispatcher(SearchAction.Search(it.text))
                },
                hint = "Family Guy",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SearchList() {

}