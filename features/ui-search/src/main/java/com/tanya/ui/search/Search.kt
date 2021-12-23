package com.tanya.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isFinite
import androidx.hilt.navigation.compose.hiltViewModel
import app.showhub.common.compose.components.PosterCard
import app.showhub.common.compose.components.SearchTextField
import app.showhub.common.compose.extensions.itemsInGrid
import app.showhub.common.compose.utils.Layout
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.statusBarsPadding
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity
import com.tanya.data.results.ShowDetailed

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
        SearchList(
            results = viewState.searchResults,
            onShowClicked = { dispatcher(SearchAction.OpenShowDetails(it.id)) },
            modifier = Modifier.bodyWidth()
        )
    }
}

@Composable
private fun SearchBox(
    state: SearchViewState,
    dispatcher: (SearchAction) -> Unit
) {
    Surface(modifier = Modifier.statusBarsPadding()) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            var searchQuery by remember {
                mutableStateOf(TextFieldValue(state.query))
            }
            Text(
                text = "Search",
                style = MaterialTheme.typography.h4,
                color = Color.White.copy(alpha = 1f),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            SearchTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    dispatcher(SearchAction.Search(it.text))
                },
                hint = "e.g. Family Guy",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SearchList(
    results: List<ShowDetailed>,
    onShowClicked: (ShowEntity) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(8.dp)
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        itemsInGrid(
            items = results,
            columns = 8/4,
            horizontalItemPadding = 16.dp,
            verticalItemPadding = 16.dp,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            SearchRow(
                show = it.show ,
                poster = it.poster,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onShowClicked(it.show) }
            )
        }
    }
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


@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.bodyWidth() = fillMaxWidth()
    .wrapContentWidth(align = Alignment.CenterHorizontally)
    .composed {
        val bodyMaxWidth = Layout.bodyMaxWidth
        if (bodyMaxWidth.isFinite) widthIn(max = bodyMaxWidth) else this
    }
    .composed {
        padding(
            rememberInsetsPaddingValues(
                insets = LocalWindowInsets.current.systemBars,
                applyBottom = false,
                applyTop = false,
            )
        )
    }