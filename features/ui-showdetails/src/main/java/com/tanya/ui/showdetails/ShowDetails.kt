package com.tanya.ui.showdetails

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.showhub.common.compose.components.*
import app.showhub.common.compose.extensions.actionButtonBackground
import app.showhub.common.compose.extensions.copy
import app.showhub.common.compose.theme.yellow400
import app.showhub.common.compose.utils.LocalShowhubDateTimeFormatter
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.tanya.common.ui.resources.R
import com.tanya.data.entities.EpisodeEntity
import com.tanya.data.entities.SeasonEntity
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity
import com.tanya.data.results.RelatedShowEntryWithShow
import com.tanya.data.results.SeasonWithEpisodesAndWatches
import kotlinx.coroutines.launch

@Composable
fun ShowDetails(
    navigateUp: () -> Unit,
    openShowDetails: (showId: Long) -> Unit
) {
    ShowDetails(
        viewModel = hiltViewModel(),
        navigateUp = navigateUp,
        openShowDetails = openShowDetails
    )
}

@Composable
internal fun ShowDetails(
    viewModel: ShowDetailsViewModel,
    navigateUp: () -> Unit,
    openShowDetails: (showId: Long) -> Unit
) {
    val viewState by rememberFlowWithLifeCycle(flow = viewModel.state)
        .collectAsState(initial = ShowDetailsViewState.EMPTY)
    ShowDetails(state = viewState) {
        when (it) {
            is ShowDetailsAction.NavigateUp -> navigateUp()
            is ShowDetailsAction.OpenShowDetails -> openShowDetails(it.showId)
            else -> viewModel.submitAction(it)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ShowDetails(
    state: ShowDetailsViewState,
    dispatcher: (ShowDetailsAction) -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val season: MutableState<SeasonEntity?> = remember { mutableStateOf(null)}
    
    BackHandler(
        enabled = modalBottomSheetState.currentValue == ModalBottomSheetValue.Expanded,
        onBack = {
            scope.launch {
                modalBottomSheetState.animateTo(ModalBottomSheetValue.Hidden)
            }
        }
    )

    ModalBottomSheetLayout(
        sheetContent = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 72.dp)
            ) {
                season.value?.let {
                    val title = it.title
                        ?: stringResource(R.string.show_season_number, it.number ?: -1)
                    PosterCard(
                        showTitle = title,
                        posterPath = it.tmdbPosterPath,
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .height(195.dp)
                            .aspectRatio(2 / 3f)
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = it.title
                            ?: stringResource(R.string.show_season_number, it.number ?: -1),
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(56.dp))
                    SheetOption(
                        icon = R.drawable.ic_open,
                        actionName = "Open",
                        action = {}
                    )
                    SheetOption(
                        icon = R.drawable.ic_play,
                        actionName = "Mark watched",
                        action = {}
                    )
                    SheetOption(
                        icon = R.drawable.ic_ignore,
                        actionName = "Ignore",
                        action = {}
                    )
                }
            }
        },
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = Color.Transparent,
        scrimColor = MaterialTheme.colors.background,
        sheetShape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .navigationBarsPadding()
    ) {
        Scaffold(
            topBar = {
                var appBarHeight by remember { mutableStateOf(0) }
                val showAppBarBackground by remember {
                    derivedStateOf {
                        val visibleItemsInfo = listState.layoutInfo.visibleItemsInfo
                        when {
                            visibleItemsInfo.isEmpty() -> false
                            appBarHeight <= 0 -> false
                            else -> {
                                val firstVisibleItem = visibleItemsInfo[0]
                                when {
                                    firstVisibleItem.index > 0 -> true
                                    else -> firstVisibleItem.size + firstVisibleItem.offset <= appBarHeight
                                }
                            }
                        }
                    }
                }
                ShowDetailsAppBar(
                    title = state.show.title,
                    following = state.isFollowed,
                    showBackground = showAppBarBackground,
                    dispatcher = dispatcher,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { appBarHeight = it.height }
                )
            }
        ) { contentPadding ->
            Surface {
                ShowDetailsContent(
                    show = state.show,
                    relatedShows = state.relatedShows,
                    seasons = state.seasons,
                    listState = listState,
                    backdrop = state.backdropImage,
                    dispatcher = dispatcher,
                    contentPadding = contentPadding,
                    modifier = Modifier.fillMaxSize()
                ) {
                    season.value = it
                    scope.launch {
                        modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                    }
                }
            }
        }
    }

}

@Composable
internal fun ShowDetailsContent(
    show: ShowEntity,
    relatedShows: List<RelatedShowEntryWithShow>,
    seasons: List<SeasonWithEpisodesAndWatches>,
    listState: LazyListState,
    backdrop: ShowImagesEntity?,
    dispatcher: (ShowDetailsAction) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    openSeasonMenu: (SeasonEntity) -> Unit
) {
    LazyColumn(
        state = listState,
        contentPadding = contentPadding.copy(copyTop = false),
        modifier = modifier
    ) {
        item {
            BackdropImage(
                show = show,
                backdrop = backdrop,
                listState = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 10)
                    .clipToBounds()
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Header(
                title = "About",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        item {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                show.certification?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.body2,
                        color = yellow400,
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = yellow400,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            )
                    )
                }
                show.summary?.let {
                    ExpandingText(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }

        if (seasons.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Header(
                    title = "Seasons",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                )
                Text(
                    text = stringResource(R.string.show_seasons_count, seasons.size),
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.65f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item {
                SeasonsGrid(
                    seasons = seasons,
                    openSeasonMenu = openSeasonMenu
                )
            }
        }

        if (relatedShows.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Header(
                    title = "People also watch",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            item {
                RelatedShows(
                    relatedShows = relatedShows,
                    dispatcher = dispatcher,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun Header(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
private fun BackdropImage(
    show: ShowEntity,
    backdrop: ShowImagesEntity?,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    var decay by remember { mutableStateOf(1f)}
    decay = 1/(listState.firstVisibleItemScrollOffset/80f)
    Surface(modifier = modifier) {
        Box {
            if (backdrop != null) {
                val image = loadPicture(url = backdrop.path).value
                image?.let { img ->
                    Image(
                        bitmap = img.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .drawForegroundGradientScrim(
                                color = Color.Black.copy(1f),
                                decay = minOf(1f, decay)
                            )
                    )
                }
            }
            BackdropContent(
                showTitle = show.title ?: "",
                rating = show.traktRating ?: 0f,
                votes = show.traktVotes ?: 0,
                decay = decay,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SheetOption(
    icon: Int,
    actionName: String,
    modifier: Modifier = Modifier,
    action: () -> Unit
) {
    Surface(
        onClick = action,
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(modifier.padding(start = 8.dp)) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = MaterialTheme.colors.onBackground.copy(0.7f),
                modifier = Modifier
                    .size(48.dp, 48.dp)
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = actionName,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground.copy(1f),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
private fun SeasonsGrid(
    seasons: List<SeasonWithEpisodesAndWatches>,
    modifier: Modifier = Modifier,
    openSeasonMenu: (SeasonEntity) -> Unit
) {
    val scrollState = rememberScrollState()

    StaggeredGrid(
        modifier = modifier
            .horizontalScroll(scrollState)
            .padding(12.dp)
    ) {
        seasons.forEach {
            SeasonChip(
                season = it.season,
                openSeasonMenu = openSeasonMenu
            )
        }
    }
}

@Composable
private fun SeasonChip(
    season: SeasonEntity,
    openSeasonMenu: (SeasonEntity) -> Unit
) {
    Surface(
        modifier = Modifier.padding(4.dp)
    ) {
        Row(modifier = Modifier.width(320.dp)) {
            PosterCard(
                showTitle = season.title
                    ?: stringResource(R.string.show_season_number, season.number!!),
                posterPath = season.tmdbPosterPath,
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .height(78.dp)
                    .aspectRatio(2 / 3f)
            )
            Column(modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(8.dp)
            ) {
                Text(
                    text = season.title
                        ?: stringResource(R.string.show_season_number, season.number!!),
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.episodes_to_watch, season.episodeCount!!),
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.65f)
                )
            }
            IconButton(
                onClick = {
                    openSeasonMenu(season)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_kebab),
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
    }
}

@Composable
private fun RelatedShows(
    relatedShows: List<RelatedShowEntryWithShow>,
    dispatcher: (ShowDetailsAction) -> Unit,
    modifier:Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

    LazyRow(
        state = listState,
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(relatedShows) {
            PosterCard(
                showTitle = it.show.title,
                posterPath = it.poster?.path,
                modifier = Modifier
                    .fillParentMaxWidth(0.2f)
                    .aspectRatio(2 / 3f)
            ) {
                dispatcher(ShowDetailsAction.OpenShowDetails(it.show.id))
            }
        }
    }
}

@Composable
private fun ShowDetailsAppBar(
    title: String?,
    following: Boolean,
    showBackground: Boolean,
    dispatcher: (ShowDetailsAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            showBackground -> Color.Black
            else -> Color.Transparent
        },
        animationSpec = tween()
    )
    
    TopAppBar(
        title = {
            Crossfade(
                targetState = showBackground && title != null,
                animationSpec = tween(1000)
            ) {
                if (it) Text(
                    text = title!!,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyBottom = false
        ),
        navigationIcon = {
            IconButton(
                onClick = { dispatcher(ShowDetailsAction.NavigateUp) },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        },
        actions = {
            Text(
                text = when {
                  following -> "Following"
                  else -> "Follow"
                },
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                color = MaterialTheme.colors.onBackground.copy(1f),
                modifier = Modifier
                    .clickable {
                        dispatcher(ShowDetailsAction.FollowShowToggleAction)
                    }
                    .actionButtonBackground(
                        enabled = !following,
                        alpha = when {
                            following -> 1f
                            else -> 0.3f
                        }
                    )
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            IconButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_refresh),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        },
        backgroundColor = backgroundColor,
        elevation = 0.dp,
        modifier = modifier
    )
}

@Composable
private fun BackdropContent(
    showTitle: String,
    rating: Float,
    votes: Int,
    decay: Float,
    modifier: Modifier = Modifier,
) {
    val titleStyle = MaterialTheme.typography.h4
    Column(modifier) {
        Text(
            text = showTitle,
            style = titleStyle.copy(
                color = Color.White,
            ),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .alpha(minOf(1f, decay))
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(R.string.trakt_rating_text, rating*10f, votes/1000f),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(minOf(1f, decay))
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun Episode(
    episode: EpisodeEntity,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(
            vertical = 16.dp
        )
    ) {
        EpisodeBackdropImage(
            path = episode.tmdbBackdropPath ?: "",
            modifier = Modifier
                .width(112.dp)
                .aspectRatio(16f / 10)
        )
        episode.firstAired?.let {
            val formatter = LocalShowhubDateTimeFormatter.current
            val formattedDate = formatter.formatShortRelativeTime(it)
            ExpandingCard(
                title = episode.title ?: "",
                description = episode.summary ?: "",
                date = formattedDate,
                icon = R.drawable.ic_calendar,
                onWatchClicked = {},
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
        }
        Text(
            text = stringResource(R.string.episode_number, episode.number ?: -1),
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun Episodes(
    episodes: List<EpisodeEntity>
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            val listState = rememberLazyListState()
            val appBarElevation by animateDpAsState(if (listState.isScrolled) 4.dp else 0.dp)
            val appBarColor = when {
                appBarElevation > 0.dp -> MaterialTheme.colors.surface
                else -> Color.Transparent
            }
            TopAppBar(
                backgroundColor = appBarColor,
                elevation = appBarElevation
            ) {
                // To be continued
            }
        }
    }
}

@Composable
fun EpisodeBackdropImage(
    path: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        val image = loadPicture(url = path).value
        image?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0