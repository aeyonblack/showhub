package com.tanya.domain.interactors

import com.tanya.base.util.AppCoroutineDispatchers
import com.tanya.data.entities.ActionDate
import com.tanya.data.repositories.episodes.SeasonsEpisodesRepository
import com.tanya.domain.Interactor
import com.tanya.domain.interactors.ChangeSeasonWatchedStatus.Action.UNWATCH
import com.tanya.domain.interactors.ChangeSeasonWatchedStatus.Action.WATCHED
import com.tanya.domain.interactors.ChangeSeasonWatchedStatus.Params
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChangeSeasonWatchedStatus @Inject constructor(
    private val repository: SeasonsEpisodesRepository,
    private val dispatchers: AppCoroutineDispatchers
): Interactor<Params>() {

    override suspend fun doWork(params: Params) = withContext(dispatchers.io) {
        when (params.action) {
            WATCHED -> repository.markSeasonWatched(
                seasonId = params.seasonId,
                onlyAired = params.onlyAired,
                date = params.actionDate
            )
            UNWATCH -> repository.markSeasonUnwatched(params.seasonId)
        }
    }

    data class Params(
        val seasonId: Long,
        val action: Action,
        val onlyAired: Boolean = true,
        val actionDate: ActionDate = ActionDate.NOW
    )

    enum class Action {
        WATCHED,
        UNWATCH
    }
}