package com.tanya.ui.library

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HorizontalRule
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.showhub.common.compose.components.FollowedShowItem
import app.showhub.common.compose.components.LazyGrid
import app.showhub.common.compose.extensions.actionButtonBackground
import app.showhub.common.compose.extensions.itemSpacer
import app.showhub.common.compose.extensions.itemsInGrid
import app.showhub.common.compose.utils.Layout
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.tanya.common.ui.resources.R.drawable.*
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
        when (it) {
            is LibraryAction.OpenShowDetails -> openShowDetails(it.showId)
            else -> viewModel.submitAction(it)
        }
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
                //sheetState.animateTo(ModalBottomSheetValue.Hidden)
                sheetState.hide()
            }
        }
    )

    if (sheetState.currentValue != ModalBottomSheetValue.Expanded)
        onHideBottomBar?.invoke(false) else {
        onHideBottomBar?.invoke(true)
    }

    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetContent(
                sortOptions = state.availableSorts,
                currentSortOption = state.sort,
                onSortSelected = { dispatcher(LibraryAction.ChangeSort(it)) }
            ) {
                scope.launch {
                    //sheetState.animateTo(ModalBottomSheetValue.Hidden)
                    sheetState.hide()
                }
            }
        },
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(8.dp),
        scrimColor = MaterialTheme.colors.background.copy(0.5f),
        modifier = Modifier.navigationBarsPadding()
    ) {
        Scaffold(
            topBar = { LibraryAppBar() },
            modifier = Modifier.fillMaxSize()
        ) {
            LibraryContent(
                contentPadding = it,
                list = list,
                currentSortOption = state.sort,
                layout = state.layout,
                dispatcher = dispatcher,
            ) {
                scope.launch {
                    //sheetState.animateTo(ModalBottomSheetValue.Expanded)
                    sheetState.hide()
                }
            }
        }
    }
}

@Composable
private fun LibraryContent(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    list: LazyPagingItems<FollowedShowEntryWithShow>,
    currentSortOption: SortOption,
    layout: LayoutType,
    dispatcher: (LibraryAction) -> Unit,
    openSortOptionsMenu: () -> Unit
) {
    Crossfade(targetState = layout) { type ->
        if (type == LayoutType.LIST) {
            ListLayout(
                list = list,
                contentPadding = contentPadding,
                currentSortOption = currentSortOption ,
                onChangeLayout = { dispatcher(LibraryAction.ChangeLayout(it)) },
                dispatcher = dispatcher,
                openSortOptionsMenu = openSortOptionsMenu
            )
        } else {
            GridLayout(
                list = list,
                contentPadding = contentPadding,
                currentSortOption = currentSortOption,
                onChangeLayout = { dispatcher(LibraryAction.ChangeLayout(it)) },
                dispatcher = dispatcher,
                openSortOptionsMenu = openSortOptionsMenu
            )
        }
    }
}

@Composable
private fun GridLayout(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    list: LazyPagingItems<FollowedShowEntryWithShow>,
    currentSortOption: SortOption,
    onChangeLayout: (LayoutType) -> Unit,
    dispatcher: (LibraryAction) -> Unit,
    openSortOptionsMenu: () -> Unit
) {
    LazyGrid(
        list = list,
        contentPadding = contentPadding,
        openShowDetails = { dispatcher(LibraryAction.OpenShowDetails(it)) }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            SortOptionButton(
                openSortOptionsMenu = openSortOptionsMenu,
                currentSortOption = currentSortOption
            )
            IconButton(
                onClick = { onChangeLayout(LayoutType.LIST) },
            ) {
                Icon(
                    painter = painterResource(id = ic_list),
                    tint = Color.White.copy(1f),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun ListLayout(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    list: LazyPagingItems<FollowedShowEntryWithShow>,
    currentSortOption: SortOption,
    onChangeLayout: (LayoutType) -> Unit,
    dispatcher: (LibraryAction) -> Unit,
    openSortOptionsMenu: () -> Unit
) {
    val columns = Layout.columns
    val bodyMargin = Layout.bodyMargin
    val gutter = Layout.gutter

    val genericItemPadding = (gutter - 8.dp).coerceAtLeast(0.dp)

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize()
    ) {

        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                SortOptionButton(
                    openSortOptionsMenu = openSortOptionsMenu,
                    currentSortOption = currentSortOption
                )
                IconButton(
                    onClick = { onChangeLayout(LayoutType.GRID) },
                ) {
                    Icon(
                        painter = painterResource(id = ic_grid),
                        tint = Color.White.copy(1f),
                        contentDescription = null
                    )
                }
            }
        }

        itemsInGrid(
            lazyPagingItems = list,
            columns = columns/4,
            contentPadding = PaddingValues(
                horizontal = (bodyMargin - 8.dp).coerceAtLeast(0.dp),
                vertical = genericItemPadding
            ),
            verticalItemPadding = genericItemPadding,
            horizontalItemPadding = genericItemPadding
        ) {
            if (it != null) {
                FollowedShowItem(
                    show = it.show,
                    poster = it.poster,
                    contentPadding = PaddingValues(8.dp),
                    onClick = { dispatcher(LibraryAction.OpenShowDetails(it.show.id)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        itemSpacer(16.dp)
    }
}

@Composable
private fun SortOptionButton(
    currentSortOption: SortOption,
    openSortOptionsMenu: () -> Unit
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
                            openSortOptionsMenu()
                        }
                    }
                )
            }
            .scale(sortOptionTransitionState.scale)

    ) {
        Icon(
            painter = painterResource(id = ic_sort_arrows),
            contentDescription = null,
            tint = Color.White.copy(alpha = sortOptionTransitionState.alpha),
            modifier = Modifier
                .align(CenterVertically)
                .size(16.dp)

        )
        Text(
            text = currentSortOption.sort,
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = sortOptionTransitionState.alpha),
            modifier = Modifier
                .align(CenterVertically)
                .padding(start = 4.dp)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SortOptionItem(
    sort: SortOption,
    selected: Boolean,
    onClick: () -> Unit
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
                            onClick()
                        }
                    }
                )
            }
            .fillMaxWidth()
            .padding(16.dp)
            .scale(sortOptionTransitionState.scale)
    ) {
        Text(
            text = sort.sort,
            style = MaterialTheme.typography.subtitle2,
            color = when {
                selected -> MaterialTheme.colors.primary
                else -> Color.White
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(CenterVertically)
        )
        AnimatedVisibility(
            visible = selected,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            Icon(
                painter = painterResource(id = ic_checkbox),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.align(CenterVertically)
            )
        }
    }
}

@Composable
private fun BottomSheetContent(
    sortOptions: List<SortOption>,
    onSortSelected: (SortOption) -> Unit,
    currentSortOption: SortOption? = null,
    closeBottomSheet: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Icon(
            imageVector = Icons.Rounded.HorizontalRule,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Sort by",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(16.dp)
        )
        for (sort in sortOptions) {
            SortOptionItem(
                sort = sort,
                selected = currentSortOption  == sort,
                onClick = {
                    onSortSelected(sort)
                    closeBottomSheet()
                }
            )
        }
        Text(
            text = "Close",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            color = MaterialTheme.colors.onBackground.copy(1f),
            modifier = Modifier
                .clickable { closeBottomSheet() }
                .actionButtonBackground(
                    enabled = true,
                    alpha = 0.15f
                )
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
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
        backgroundColor = Color.Black,
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

private enum class PressState { PRESSED, RELEASED }

enum class LayoutType(val type: String) {
    GRID("grid"),
    LIST("list")
}