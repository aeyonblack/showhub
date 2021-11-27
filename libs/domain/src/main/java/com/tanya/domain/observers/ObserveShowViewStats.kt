package com.tanya.domain.observers

import com.tanya.data.repositories.followedshows.FollowedShowsRepository
import com.tanya.data.views.FollowedShowsWatchStats
import com.tanya.domain.SubjectInteractor
import com.tanya.domain.observers.ObserveShowViewStats.Params
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveShowViewStats @Inject constructor(
    private val repository: FollowedShowsRepository
) : SubjectInteractor<Params, FollowedShowsWatchStats?>() {

    /*override fun createObservable(params: Params) =
        repository.observeShowViewStats(params.showId)*/

    override fun createObservable(params: Params): Flow<FollowedShowsWatchStats?> {
        TODO("Not yet implemented")
    }

    data class Params(val showId: Long)
}