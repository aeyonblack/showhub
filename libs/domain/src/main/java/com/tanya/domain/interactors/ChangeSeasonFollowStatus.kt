package com.tanya.domain.interactors

import com.tanya.data.repositories.episodes.SeasonsEpisodesRepository
import com.tanya.domain.Interactor
import com.tanya.domain.interactors.ChangeSeasonFollowStatus.Action.*
import com.tanya.domain.interactors.ChangeSeasonFollowStatus.Params
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChangeSeasonFollowStatus @Inject constructor(
    private val repository: SeasonsEpisodesRepository
): Interactor<Params>() {

    override suspend fun doWork(params: Params) = withContext(Dispatchers.IO) {
        when (params.action) {
            FOLLOW -> repository.markSeasonFollowed(params.seasonId)
            IGNORE -> repository.markSeasonIgnored(params.seasonId)
            IGNORE_PREVIOUS -> repository.markPreviousSeasonsIgnored(params.seasonId)
        }
    }

    data class Params(val seasonId: Long, val action: Action)

    enum class Action {
        FOLLOW,
        IGNORE,
        IGNORE_PREVIOUS
    }
}