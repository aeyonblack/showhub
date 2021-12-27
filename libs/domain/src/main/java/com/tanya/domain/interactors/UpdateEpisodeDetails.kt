package com.tanya.domain.interactors

import com.tanya.data.repositories.episodes.SeasonsEpisodesRepository
import com.tanya.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateEpisodeDetails @Inject constructor(
    private val seasonsEpisodesRepository: SeasonsEpisodesRepository
) : Interactor<UpdateEpisodeDetails.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(Dispatchers.IO) {
            seasonsEpisodesRepository.updateEpisode(params.episodeId)
        }
    }

    data class Params(val episodeId: Long, val forceLoad: Boolean = true)
}