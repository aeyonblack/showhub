package com.tanya.ui.showdetails

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity
import com.tanya.data.results.EpisodeWithSeason
import com.tanya.data.results.RelatedShowEntryWithShow
import com.tanya.data.results.SeasonWithEpisodesAndWatches
import com.tanya.data.views.FollowedShowsWatchStats

@Immutable
internal data class ShowDetailsViewState(
    val isFollowed: Boolean = false,
    val show: ShowEntity = ShowEntity.EMPTY_SHOW,
    val backdropImage: ShowImagesEntity? = null,
    val relatedShows: List<RelatedShowEntryWithShow> = emptyList(),
    val seasons: List<SeasonWithEpisodesAndWatches> = emptyList(),
    val pagedSeasons: PagingData<SeasonWithEpisodesAndWatches> = PagingData.empty(),
    val nextEpisodeToWatch: EpisodeWithSeason? = null,
    val watchStats: FollowedShowsWatchStats? = null,
    val refreshing: Boolean = false
) {
    companion object {
        val EMPTY = ShowDetailsViewState()
    }
}