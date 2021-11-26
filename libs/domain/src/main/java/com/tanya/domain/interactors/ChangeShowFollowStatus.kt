package com.tanya.domain.interactors

import android.util.Log
import com.tanya.data.android.repository.images.ShowImagesStore
import com.tanya.data.android.repository.shows.ShowsStore
import com.tanya.data.extensions.fetch
import com.tanya.data.repositories.episodes.SeasonsEpisodesRepository
import com.tanya.data.repositories.followedshows.FollowedShowsRepository
import com.tanya.domain.Interactor
import com.tanya.domain.interactors.ChangeShowFollowStatus.Action.*
import com.tanya.domain.interactors.ChangeShowFollowStatus.Params
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChangeShowFollowStatus @Inject constructor(
    private val followedShowsRepository: FollowedShowsRepository,
    private val seasonsEpisodesRepository: SeasonsEpisodesRepository,
    private val showsStore: ShowsStore,
    private val showImagesStore: ShowImagesStore
) : Interactor<Params>() {

    override suspend fun doWork(params: Params) = withContext(Dispatchers.IO) {
        params.showIds.forEach {
            when (params.action) {
                TOGGLE ->
                    if (followedShowsRepository.isShowFollowed(it)) unfollow(it)
                    else follow(it, params.deferDataFetch)
                FOLLOW -> follow(it, params.deferDataFetch)
                UNFOLLOW -> unfollow(it)
            }
        }

        val result = followedShowsRepository.syncFollowedShows()
        result.added.forEach {
            showsStore.fetch(it.showId)
            try {
                showImagesStore.fetch(it.showId)
            } catch (t: Throwable) {
                Log.d("changeSeasonFollow", "Error fetching images for " +
                        "show id ${it.showId}")
            }
        }
    }

    private suspend fun follow(showId: Long, deferDataFetch: Boolean) {
        followedShowsRepository.addFollowedShow(showId)
        if (!deferDataFetch) {
            seasonsEpisodesRepository.updateSeasonsEpisodes(showId)
            seasonsEpisodesRepository.updateShowEpisodeWatches(showId, forceRefresh = true)
        }
    }

    private suspend fun unfollow(showId: Long) {
        followedShowsRepository.removeFollowedShow(showId)
        seasonsEpisodesRepository.removeShowSeasonData(showId)
    }

    data class Params(
        val showIds: Collection<Long>,
        val action: Action,
        val deferDataFetch: Boolean = false
    )

    enum class Action {
        FOLLOW,
        UNFOLLOW,
        TOGGLE
    }
}