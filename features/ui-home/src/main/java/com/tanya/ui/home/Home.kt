package com.tanya.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.showhub.common.compose.components.AppBar
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
                Spacer(modifier = Modifier.height(40.dp))
            }
            /*trending items*/
            item {

            }
            /*popular items*/
            item {

            }
        }
    }
}

@Composable
private fun <T: EntryWithShow<*>> CarouselWithHeader(
    items: List<T>,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(modifier) {

    }
}

@Composable
private fun Header(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit = {}
) {
    Row(modifier) {
        Spacer(modifier = Modifier.width(32.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.h4,
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

}