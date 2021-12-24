package com.tanya.ui.library

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.tanya.data.results.FollowedShowEntryWithShow
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun Library(
    state: LibraryViewState,
    list: LazyPagingItems<FollowedShowEntryWithShow>,
    dispatcher: (LibraryAction) -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    BackHandler(
        enabled = sheetState.currentValue == ModalBottomSheetValue.Expanded,
        onBack = {
            scope.launch {
                sheetState.animateTo(ModalBottomSheetValue.Hidden)
            }
        }
    )

    ModalBottomSheetLayout(
        sheetContent = {
            Column() {
                Text(text = "modal", style = MaterialTheme.typography.h4)
                Text(text = "modal", style = MaterialTheme.typography.h4)
                Text(text = "modal", style = MaterialTheme.typography.h4)
                Text(text = "modal", style = MaterialTheme.typography.h4)
                Text(text = "modal", style = MaterialTheme.typography.h4)
            }
        },
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(2.dp)
    ) {
        Scaffold(
            topBar = { LibraryAppBar() },
            modifier = Modifier.fillMaxSize()
        ) {

        }
    }
}

@Composable
private fun LibraryAppBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = "Your Library",
                style = MaterialTheme.typography.h5,
            )
        },
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyBottom = false
        ),
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        modifier = modifier.fillMaxWidth()
    )
}