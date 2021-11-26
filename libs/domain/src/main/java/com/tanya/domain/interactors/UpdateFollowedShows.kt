package com.tanya.domain.interactors

import android.util.Log
import com.tanya.data.android.repository.images.ShowImagesStore
import com.tanya.data.android.repository.shows.ShowsStore
import com.tanya.data.daos.ShowDao
import com.tanya.data.entities.RefreshType
import com.tanya.data.extensions.fetch
import com.tanya.data.repositories.episodes.SeasonsEpisodesRepository
import com.tanya.data.repositories.followedshows.FollowedShowsRepository
import com.tanya.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateFollowedShows @Inject constructor(
    private val followedShowsRepository: FollowedShowsRepository,
    private val seasonsEpisodesRepository: SeasonsEpisodesRepository,
    private val showsStore: ShowsStore,
    private val showImagesStore: ShowImagesStore,
    private val showDao: ShowDao
) : Interactor<UpdateFollowedShows.Params>() {

    override suspend fun doWork(params: Params) = withContext(Dispatchers.IO) {
        if (params.forceRefresh || followedShowsRepository.needFollowedShowsSync()) {
            followedShowsRepository.syncFollowedShows()
        }

        followedShowsRepository.getFollowedShows().forEach {
            ensureActive()
            showsStore.fetch(it.showId)
            try {
                showImagesStore.fetch(it.showId)
            } catch (t: Throwable) {
                Log.d("updateFollowedShows", "Error fetching images for " +
                        "show ${it.showId}")
            }

            ensureActive()
            if (params.forceRefresh || seasonsEpisodesRepository.needShowSeasonsUpdate(it.showId)) {
                seasonsEpisodesRepository.updateSeasonsEpisodes(it.showId)
            }

            ensureActive()
            seasonsEpisodesRepository.updateShowEpisodeWatches(
                showId = it.showId,
                refreshType = params.type,
                forceRefresh = params.forceRefresh,
                lastUpdated = showDao.getShowWithId(it.showId)?.traktDataUpdate
            )
        }
    }

    data class Params(val forceRefresh: Boolean, val type: RefreshType)
}