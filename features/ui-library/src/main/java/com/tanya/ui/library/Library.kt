package com.tanya.ui.library

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.showhub.common.compose.extensions.itemsInGrid
import app.showhub.common.compose.utils.Layout
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.tanya.common.ui.resources.R.drawable.ic_sort_arrows
import com.tanya.data.entities.SortOption
import com.tanya.data.results.FollowedShowEntryWithShow
import kotlinx.coroutines.launch

@Composable
fun Library(
    openShowDetails: (showId: Long) -> Unit,
    onHideBottomBar: ((Boolean) -> Unit)?
) {
    Library(
        viewModel = hiltViewModel(),
        openShowDetails = openShowDetails,
        onHideBottomBar = onHideBottomBar
    )
}

@Composable
internal fun Library(
    viewModel: LibraryViewModel,
    openShowDetails: (showId: Long) -> Unit,
    onHideBottomBar: ((Boolean) -> Unit)?
) {
    val viewState by rememberFlowWithLifeCycle(flow = viewModel.state)
        .collectAsState(initial = LibraryViewState.Empty)
    Library(
        state = viewState,
        onHideBottomBar = onHideBottomBar,
        list = rememberFlowWithLifeCycle(flow = viewModel.pagedList).collectAsLazyPagingItems()
    ) {

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun Library(
    state: LibraryViewState,
    onHideBottomBar: ((Boolean) -> Unit)?,
    list: LazyPagingItems<FollowedShowEntryWithShow>,
    dispatcher: (LibraryAction) -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    BackHandler(
        enabled = sheetState.currentValue == ModalBottomSheetValue.Expanded,
        onBack = {
            scope.launch {
                onHideBottomBar?.invoke(false)
                sheetState.animateTo(ModalBottomSheetValue.Hidden)
            }
        }
    )

    if (sheetState.currentValue != ModalBottomSheetValue.Expanded) onHideBottomBar?.invoke(false)

    ModalBottomSheetLayout(
        sheetContent = {
            Column {
                Text(text = "modal", style = MaterialTheme.typography.h4)
                Text(text = "modal", style = MaterialTheme.typography.h4)
                Text(text = "modal", style = MaterialTheme.typography.h4)
                Text(text = "modal", style = MaterialTheme.typography.h4)
                Text(text = "modal", style = MaterialTheme.typography.h4)
            }
        },
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(2.dp),
        scrimColor = MaterialTheme.colors.background.copy(0.35f),
        modifier = Modifier.navigationBarsPadding()
    ) {
        Scaffold(
            topBar = { LibraryAppBar() },
            modifier = Modifier.fillMaxSize()
        ) {
            LibraryContent(contentPadding = it) {
                scope.launch {
                    onHideBottomBar?.invoke(true)
                    sheetState.animateTo(ModalBottomSheetValue.Expanded)
                }
            }
        }
    }
}

@Composable
private fun LibraryContent(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    list: LazyPagingItems<FollowedShowEntryWithShow>,
    openSortOptions: (SortOption) -> Unit
) {
    val columns = Layout.columns
    val bodyMargin = Layout.bodyMargin
    val gutter = Layout.gutter

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize()
    ) {
        item { SortOptionButton(openSortOptions = openSortOptions) }

    }
}

@Composable
private fun SortOptionButton(
    openSortOptions: (SortOption) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val sortOptionTransitionState = sortOptionTransition(pressed = isPressed)

    Row(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        try {
                            isPressed = true
                            awaitRelease()
                        } finally {
                            isPressed = false
                            openSortOptions(SortOption.SUPER_SORT)
                        }
                    }
                )
            }
            .padding(16.dp)
            .scale(sortOptionTransitionState.scale)

    ) {
        Icon(
            painter = painterResource(id = ic_sort_arrows),
            contentDescription = null,
            tint = Color.White.copy(alpha = sortOptionTransitionState.alpha),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(16.dp)

        )
        Text(
            text = "Alphabetical Order",
            style = MaterialTheme.typography.subtitle2,
            color = Color.White.copy(alpha = sortOptionTransitionState.alpha),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 4.dp)
        )
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

@Composable
private fun sortOptionTransition(pressed: Boolean) : SortOptionTransition {
    val transition = updateTransition(
        targetState = if (pressed) PressState.PRESSED else PressState.RELEASED, label = ""
    )
    val scale = transition.animateFloat(label = "scale animation") {
        when (it) {
            PressState.PRESSED -> 0.9f
            PressState.RELEASED -> 1f
        }
    }

    val alpha = transition.animateFloat(label = "alpha animation") {
        when (it) {
            PressState.PRESSED -> 0.7f
            PressState.RELEASED -> 1f
        }
    }

    return remember(transition) {
        SortOptionTransition(scale = scale, alpha = alpha)
    }
}

private class SortOptionTransition(
    scale: State<Float>,
    alpha: State<Float>
) {
    val scale by scale
    val alpha by alpha
}

private enum class PressState {PRESSED, RELEASED}