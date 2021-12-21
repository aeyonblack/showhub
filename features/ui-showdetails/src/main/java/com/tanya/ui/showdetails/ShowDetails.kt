package com.tanya.ui.showdetails

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
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
import androidx.compose.ui.unit.Dp
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
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity
import com.tanya.data.results.EpisodeWithWatches
import com.tanya.data.results.RelatedShowEntryWithShow
import com.tanya.data.results.SeasonWithEpisodesAndWatches
import com.tanya.ui.showdetails.SelectionState.SELECTED
import com.tanya.ui.showdetails.SelectionState.UNSELECTED
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
    var isEpisodesSheetOpen by remember { mutableStateOf(false)}
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val seasonWithEpisodes: MutableState<SeasonWithEpisodesAndWatches?> = remember { mutableStateOf(null)}

    BoxWithConstraints {

        BackHandler(
            enabled = modalBottomSheetState.currentValue == ModalBottomSheetValue.Expanded
                    || isEpisodesSheetOpen,
            onBack = {
                scope.launch {
                    modalBottomSheetState.animateTo(ModalBottomSheetValue.Hidden)
                }
                if (isEpisodesSheetOpen) isEpisodesSheetOpen = false
            }
        )

        Box {
            ModalBottomSheetLayout(
                sheetContent = {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 72.dp)
                    ) {
                        seasonWithEpisodes.value?.season?.let {
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
                            modifier = Modifier.fillMaxSize(),
                            openSeasonDetails = {
                                seasonWithEpisodes.value = it
                                isEpisodesSheetOpen = true
                            }
                        ) {
                            seasonWithEpisodes.value = it
                            scope.launch {
                                modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                            }
                        }
                    }
                }
            }
            if (isEpisodesSheetOpen) {
                EpisodesSheet(
                    episodes = seasonWithEpisodes.value?.episodes ?: listOf(),
                    seasonTitle = seasonWithEpisodes.value?.season?.title,
                    seasonNumber = seasonWithEpisodes.value?.season?.number
                ) {
                    isEpisodesSheetOpen = it == SheetState.OPEN
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
    openSeasonDetails: (SeasonWithEpisodesAndWatches) -> Unit,
    openSeasonMenu: (SeasonWithEpisodesAndWatches) -> Unit
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
                    openSeasonDetails = openSeasonDetails,
                    openSeasonMenu = openSeasonMenu,
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
    openSeasonDetails: (SeasonWithEpisodesAndWatches) -> Unit,
    openSeasonMenu: (SeasonWithEpisodesAndWatches) -> Unit
) {
    val scrollState = rememberScrollState()

    StaggeredGrid(
        modifier = modifier
            .horizontalScroll(scrollState)
            .padding(12.dp)
    ) {
        seasons.forEach {
            SeasonChip(
                seasonWithEpisodes = it,
                openSeasonDetails = openSeasonDetails,
                openSeasonMenu = openSeasonMenu
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SeasonChip(
    seasonWithEpisodes: SeasonWithEpisodesAndWatches,
    openSeasonDetails: (SeasonWithEpisodesAndWatches) -> Unit,
    openSeasonMenu: (SeasonWithEpisodesAndWatches) -> Unit
) {
    Surface(
        onClick = { openSeasonDetails(seasonWithEpisodes) },
        modifier = Modifier.padding(4.dp),
    ) {
        Row(modifier = Modifier.width(320.dp)) {
            PosterCard(
                showTitle = seasonWithEpisodes.season.title
                    ?: stringResource(R.string.show_season_number, seasonWithEpisodes.season.number!!),
                posterPath = seasonWithEpisodes.season.tmdbPosterPath,
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
                    text = seasonWithEpisodes.season.title
                        ?: stringResource(R.string.show_season_number, seasonWithEpisodes.season.number!!),
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.episodes_to_watch, seasonWithEpisodes.season.episodeCount!!),
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.65f)
                )
            }
            IconButton(
                onClick = {
                    openSeasonMenu(seasonWithEpisodes)
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
                        enabled = !following
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Episode(
    episode: EpisodeEntity,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        onClick = { expanded = !expanded }
    ) {
        Row(
            modifier = modifier.padding(
                vertical = 16.dp
            )
        ) {
            EpisodeBackdropImage(
                path = episode.tmdbBackdropPath ?: "null",
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
                    episodeNumber = episode.number,
                    date = formattedDate,
                    icon = R.drawable.ic_calendar,
                    expanded = expanded,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun EpisodesSheet(
    episodes: List<EpisodeWithWatches>,
    seasonTitle: String? = null,
    seasonNumber: Int? =  null,
    updateSheetState: (SheetState) -> Unit
) {
    Surface {
        Episodes(
            episodes = episodes,
            seasonTitle = seasonTitle,
            seasonNumber = seasonNumber,
            updateSheetState = updateSheetState
        )
    }
}

@Composable
private fun Episodes(
    episodes: List<EpisodeWithWatches>,
    seasonTitle: String? = null,
    seasonNumber: Int? = null,
    updateSheetState: (SheetState) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            val listState = rememberLazyListState()
            TopAppBar(
                backgroundColor = Color.Transparent,
            ) {
                Text(
                    text = seasonTitle
                        ?: stringResource(R.string.show_season_number, seasonNumber ?: -1),
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                IconButton(
                    onClick = { updateSheetState(SheetState.CLOSED) },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null
                    )
                }
            }
            LazyColumn(
                state = listState,
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.systemBars,
                    applyTop = false
                )
            ) {
                items(episodes) {
                    Episode(episode = it.episode)
                    Divider(startIndent = 128.dp)
                }
            }
        }
    }
}

@Composable
private fun EpisodeBackdropImage(
    path: String,
    modifier: Modifier = Modifier
) {
    Log.d("episodeBackdropImage", "path = $path")

    Surface(

    ) {
        Box(
            modifier = modifier,
        ) {
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
}

@Composable
private fun episodeImageTransition(episodeWatched: Boolean): EpisodeImageTransition {
    val transition = updateTransition(
        targetState = if (episodeWatched) SELECTED else UNSELECTED
    )
    val corerRadius = transition.animateDp { state ->
        when (state) {
            UNSELECTED -> 0.dp
            SELECTED -> 28.dp
        }
    }
    val selectedAlpha = transition.animateFloat { state ->
        when (state) {
            UNSELECTED -> 0f
            SELECTED -> 0.8f
        }
    }
    val checkScale = transition.animateFloat { state ->
        when (state) {
            SELECTED -> 0.6f
            UNSELECTED -> 1f
        }
    }
    return remember(transition) {
        EpisodeImageTransition(corerRadius, selectedAlpha, checkScale)
    }
}

private class EpisodeImageTransition(
    cornerRadius: State<Dp>,
    selectedAlpha: State<Float>,
    checkScale: State<Float>
) {
    val cornerRadius by cornerRadius
    val selectedAlpha by selectedAlpha
    val checkScale by checkScale
}

private enum class SelectionState {SELECTED, UNSELECTED}

private enum class SheetState {OPEN, CLOSED}
