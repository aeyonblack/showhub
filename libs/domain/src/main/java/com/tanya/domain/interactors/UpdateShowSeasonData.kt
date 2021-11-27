package com.tanya.domain.interactors

import com.tanya.data.repositories.episodes.SeasonsEpisodesRepository
import com.tanya.data.repositories.followedshows.FollowedShowsRepository
import com.tanya.domain.Interactor
import com.tanya.domain.interactors.UpdateShowSeasonData.Params
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateShowSeasonData @Inject constructor(
    private val seasonsEpisodesRepository: SeasonsEpisodesRepository,
    private val followedShowsRepository: FollowedShowsRepository
) : Interactor<Params>() {

    override suspend fun doWork(params: Params) = withContext(Dispatchers.IO) {
        if (followedShowsRepository.isShowFollowed(params.showId)) {
            if (params.forceRefresh /*|| seasonsEpisodesRepository.needShowSeasonsUpdate(params.showId)*/) {
                seasonsEpisodesRepository.updateSeasonsEpisodes(params.showId)
            }

            ensureActive()
            if (params.forceRefresh /*|| seasonsEpisodesRepository.needShowEpisodeWatchesSync(params.showId)*/) {
                seasonsEpisodesRepository.syncEpisodeWatchesForShow(params.showId)
            }
        } else {
            seasonsEpisodesRepository.removeShowSeasonData(params.showId)
        }
    }

    data class Params(val showId: Long, val forceRefresh: Boolean = false)
}