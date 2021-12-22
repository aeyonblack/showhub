package com.tanya.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.showhub.common.compose.components.PosterCard
import app.showhub.common.compose.components.SearchTextField
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle
import com.google.accompanist.insets.statusBarsPadding
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity

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

@Composable
private fun SearchRow(
    show: ShowEntity,
    poster: ShowImagesEntity?,
    modifier: Modifier = Modifier
) {
    Row(modifier.padding(vertical = 8.dp)) {
        PosterCard(
            showTitle = show.title,
            posterPath = poster?.path,
            modifier = Modifier
                .fillMaxWidth(0.2f)
                .aspectRatio(2 / 3f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = show.title ?: "",
                style = MaterialTheme.typography.subtitle2
            )
            if (show.summary?.isNotEmpty() == true) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = show.summary!!,
                        style = MaterialTheme.typography.caption,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                }
            }
        }
    }
}