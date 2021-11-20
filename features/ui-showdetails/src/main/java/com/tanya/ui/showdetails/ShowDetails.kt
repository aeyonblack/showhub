package com.tanya.ui.showdetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import app.showhub.common.compose.components.AppBar
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle
import com.google.accompanist.insets.statusBarsPadding
import com.tanya.common.ui.resources.R

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
    ConstraintLayout() {
        val (topAppBar, content) = createRefs()
        AppBar(
            title = state.show.title ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .constrainAs(topAppBar) {
                    top.linkTo(parent.top)
                }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(content) {
                    top.linkTo(parent.top)
                }
        ) {
            repeat(50) {
                item {
                    Text(
                        text = "List Content",
                    )
                }
            }
        }
    }
}

@Composable
internal fun AppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(16.dp)
        )
    }
}