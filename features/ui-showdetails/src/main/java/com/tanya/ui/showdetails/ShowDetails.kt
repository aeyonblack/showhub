package com.tanya.ui.showdetails

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.showhub.common.compose.components.*
import app.showhub.common.compose.extensions.actionButtonBackground
import app.showhub.common.compose.extensions.copy
import app.showhub.common.compose.theme.yellow400
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.tanya.common.ui.resources.R
import com.tanya.data.entities.SeasonEntity
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity
import com.tanya.data.results.RelatedShowEntryWithShow
import com.tanya.data.results.SeasonWithEpisodesAndWatches
import com.tanya.data.results.numberAiredToWatch
import com.tanya.data.results.numberWatched

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

@Composable
internal fun ShowDetails(
    state: ShowDetailsViewState,
    dispatcher: (ShowDetailsAction) -> Unit
) {
    val listState = rememberLazyListState()
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
            )
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
    modifier: Modifier = Modifier
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
            Header(title = "About")
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
                Header(title = "Seasons")
            }

            item {
                SeasonsGrid(
                    seasons = seasons,
                )
            }
        }

        if (relatedShows.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Header(title = "People also watch")
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
    title: String
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f)
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

@Composable
private fun SeasonsGrid(
    seasons: List<SeasonWithEpisodesAndWatches>,
    modifier: Modifier = Modifier
) {
    StaggeredGrid(
        modifier = modifier
        .horizontalScroll(rememberScrollState())
        .padding(8.dp)
    ) {
        seasons.forEach {
            SeasonChip(
                season = it.season,
                episodesToWatch = it.episodes.numberAiredToWatch,
                episodesWatched = it.episodes.numberWatched
            )
        }
    }
}

@Composable
private fun SeasonChip(
    season: SeasonEntity,
    episodesToWatch: Int,
    episodesWatched: Int
) {
    Surface(
        modifier = Modifier.padding(4.dp)
    ) {
        Row {
            PosterCard(
                showTitle = season.title
                    ?: stringResource(R.string.show_season_number, season.number!!),
                posterPath = season.tmdbPosterPath,
                //shape = RoundedCornerShape(0f)
            )
            Column {
                Text(
                    text = season.title
                        ?: stringResource(R.string.show_season_number, season.number!!)
                )
                Text(
                    text = stringResource(R.string.episodes_to_watch, episodesToWatch)
                )
                Text(
                    text = stringResource(R.string.episodes_watched, episodesWatched)
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = null,
                    tint = Color.White
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

    //var enabled by remember { mutableStateOf(true) }
    
    TopAppBar(
        title = {
            Crossfade(
                targetState = showBackground && title != null,
                animationSpec = tween(1000)
            ) {
                if (it) Text(text = title!!)
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