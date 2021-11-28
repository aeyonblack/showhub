package com.tanya.ui.showdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanya.base.base.InvokeError
import com.tanya.base.base.InvokeStarted
import com.tanya.base.base.InvokeStatus
import com.tanya.base.base.InvokeSuccess
import com.tanya.base.extensions.combine
import com.tanya.common.ui.view.util.ObservableLoadingCounter
import com.tanya.domain.interactors.*
import com.tanya.domain.interactors.ChangeSeasonWatchedStatus.Action
import com.tanya.domain.observers.*
import com.tanya.ui.showdetails.ShowDetailsAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ShowDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val updateShowDetails: UpdateShowDetails,
    observeShowDetails: ObserveShowDetails,
    private val updateShowImages: UpdateShowImages,
    observeShowImages: ObserveShowImages,
    private val updateRelatedShows: UpdateRelatedShows,
    observeRelatedShows: ObserveRelatedShows,
    private val updateShowSeasons: UpdateShowSeasonData,
    observeShowSeasons: ObserveShowSeasonsEpisodesWatches,
    private val changeSeasonWatchedStatus: ChangeSeasonWatchedStatus,
    observeNextEpisodeToWatch: ObserveShowNextEpisodeToWatch,
    private val changeShowFollowStatus: ChangeShowFollowStatus,
    observeShowFollowStatus: ObserveShowFollowStatus,
    private val changeSeasonFollowStatus: ChangeSeasonFollowStatus,
    observeShowViewStats: ObserveShowViewStats,
) : ViewModel() {
    private val showId: Long = savedStateHandle.get("showId")!!
    private val loadingState = ObservableLoadingCounter()

    private val pendingActions = MutableSharedFlow<ShowDetailsAction>()

    val state = combine(
        loadingState.observable,
        observeShowDetails.flow,
        observeShowImages.flow,
        observeRelatedShows.flow,
        observeShowSeasons.flow,
        observeNextEpisodeToWatch.flow,
        observeShowFollowStatus.flow,
        observeShowViewStats.flow
    ) { refreshing, show, showImages, relatedShows, seasons, nextEpisode,
        isFollowed, stats ->
        ShowDetailsViewState(
            show = show,
            backdropImage = showImages.backdrop,
            relatedShows = relatedShows,
            refreshing = refreshing,
            seasons = seasons,
            nextEpisodeToWatch = nextEpisode,
            isFollowed = isFollowed,
            watchStats = stats
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ShowDetailsViewState.EMPTY
    )

    init {

        viewModelScope.launch {
            pendingActions.collect {
                when (it) {
                    is ChangeSeasonFollowedAction -> onChangeSeasonFollowStatus(it)
                    FollowShowToggleAction -> onToggleFollowButtonClicked()
                    is MarkSeasonUnwatchedAction -> onMarkSeasonUnwatched(it)
                    is MarkSeasonWatchedAction -> onMarkSeasonWatched(it)
                    is UnfollowPreviousSeasonsFollowed -> onUnfollowPreviousSeasonsFollowed(it)
                    else -> {}
                }
            }
        }

        observeShowDetails(ObserveShowDetails.Params(showId))
        observeShowImages(ObserveShowImages.Params(showId))
        observeRelatedShows(ObserveRelatedShows.Params(showId))
        observeShowFollowStatus(ObserveShowFollowStatus.Params(showId))
        observeShowSeasons(ObserveShowSeasonsEpisodesWatches.Params(showId))
        observeNextEpisodeToWatch(ObserveShowNextEpisodeToWatch.Params(showId))
        observeShowViewStats(ObserveShowViewStats.Params(showId))

        refresh()
    }

    fun submitAction(action: ShowDetailsAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    private fun refresh(forceLoad: Boolean = true) {
        updateShowDetails(UpdateShowDetails.Params(showId, forceLoad)).watchStatus()
        updateShowImages(UpdateShowImages.Params(showId, forceLoad)).watchStatus()
        updateRelatedShows(UpdateRelatedShows.Params(showId, forceLoad)).watchStatus()
        updateShowSeasons(UpdateShowSeasonData.Params(showId, forceLoad)).watchStatus()
    }

    private fun Flow<InvokeStatus>.watchStatus() = viewModelScope.launch { collectStatus() }

    private suspend fun Flow<InvokeStatus>.collectStatus() = collect {
        when(it) {
            InvokeStarted -> loadingState.addLoader()
            InvokeSuccess -> loadingState.removeLoader()
            is InvokeError -> {
                loadingState.removeLoader()
            }
        }
    }

    private fun onToggleFollowButtonClicked() {
        viewModelScope.launch {
            changeShowFollowStatus(
                ChangeShowFollowStatus.Params(
                    showId,
                    ChangeShowFollowStatus.Action.TOGGLE
                )
            ).watchStatus()
            Log.d("showDetailsVM", "onToggleFollowButtonClicked")
        }
    }

    private fun onMarkSeasonWatched(action: MarkSeasonWatchedAction) {
        changeSeasonWatchedStatus(
            ChangeSeasonWatchedStatus.Params(
                seasonId = action.seasonId,
                action = Action.WATCHED,
                onlyAired = action.onlyAired,
                actionDate = action.date
            )
        ).watchStatus()
    }

    private fun onMarkSeasonUnwatched(action: MarkSeasonUnwatchedAction) {
        changeSeasonWatchedStatus(
            ChangeSeasonWatchedStatus.Params(
                seasonId = action.seasonId,
                action = Action.UNWATCH
            )
        ).watchStatus()
    }

    private fun onChangeSeasonFollowStatus(action: ChangeSeasonFollowedAction) {
        changeSeasonFollowStatus(
            ChangeSeasonFollowStatus.Params(
                seasonId = action.seasonId,
                action = when {
                    action.followed -> ChangeSeasonFollowStatus.Action.FOLLOW
                    else -> ChangeSeasonFollowStatus.Action.IGNORE
                }
            )
        ).watchStatus()
    }

    private fun onUnfollowPreviousSeasonsFollowed(action: UnfollowPreviousSeasonsFollowed) {
        changeSeasonFollowStatus(
            ChangeSeasonFollowStatus.Params(
                seasonId = action.seasonId,
                action = ChangeSeasonFollowStatus.Action.IGNORE_PREVIOUS
            )
        ).watchStatus()
    }
}