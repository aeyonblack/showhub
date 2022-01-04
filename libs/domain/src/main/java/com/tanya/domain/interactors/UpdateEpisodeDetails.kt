package com.tanya.domain.interactors

import com.tanya.base.util.AppCoroutineDispatchers
import com.tanya.data.repositories.episodes.SeasonsEpisodesRepository
import com.tanya.domain.Interactor
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateEpisodeDetails @Inject constructor(
    private val seasonsEpisodesRepository: SeasonsEpisodesRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateEpisodeDetails.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            seasonsEpisodesRepository.updateEpisode(params.episodeId)
        }
    }

    data class Params(val episodeId: Long, val forceLoad: Boolean = true)
}