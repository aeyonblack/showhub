package com.tanya.ui.showdetails

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
import com.tanya.domain.interactors.ChangeSeasonFollowStatus.*
import com.tanya.domain.interactors.ChangeSeasonFollowStatus.Action.*
import com.tanya.domain.interactors.ChangeSeasonWatchedStatus.Action.UNWATCH
import com.tanya.domain.interactors.ChangeSeasonWatchedStatus.Action.WATCHED
import com.tanya.domain.interactors.ChangeShowFollowStatus.Action.TOGGLE
import com.tanya.domain.interactors.ChangeShowFollowStatus.Params
import com.tanya.domain.observers.*
import com.tanya.ui.showdetails.ShowDetailsAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
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
        observeShowDetails(ObserveShowDetails.Params(showId))
        observeShowImages(ObserveShowImages.Params(showId))
        observeRelatedShows(ObserveRelatedShows.Params(showId))

        refresh()
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
            changeShowFollowStatus(Params(showId, TOGGLE)).watchStatus()
        }
    }

    private fun onMarkSeasonWatched(action: MarkSeasonWatchedAction) {
        changeSeasonWatchedStatus(
            ChangeSeasonWatchedStatus.Params(
                seasonId = action.seasonId,
                action = WATCHED,
                onlyAired = action.onlyAired,
                actionDate = action.date
            )
        ).watchStatus()
    }

    private fun onMarkSeasonUnwatched(action: MarkSeasonUnwatchedAction) {
        changeSeasonWatchedStatus(
            ChangeSeasonWatchedStatus.Params(
                seasonId = action.seasonId,
                action = UNWATCH
            )
        ).watchStatus()
    }

    private fun onChangeSeasonFollowStatus(action: ChangeSeasonFollowedAction) {
        changeSeasonFollowStatus(
            Params(
                seasonId = action.seasonId,
                action = when {
                    action.followed -> FOLLOW
                    else -> IGNORE
                }
            )
        ).watchStatus()
    }

    private fun onUnfollowPreviousSeasonsFollowed(action: UnfollowPreviousSeasonsFollowed) {
        changeSeasonFollowStatus(
            ChangeSeasonFollowStatus.Params(
                seasonId = action.seasonId,
                action = IGNORE_PREVIOUS
            )
        ).watchStatus()
    }
}