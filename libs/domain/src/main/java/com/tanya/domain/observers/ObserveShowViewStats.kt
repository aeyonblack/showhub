package com.tanya.domain.observers

import com.tanya.data.repositories.followedshows.FollowedShowsRepository
import com.tanya.data.views.FollowedShowsWatchStats
import com.tanya.domain.SubjectInteractor
import com.tanya.domain.observers.ObserveShowViewStats.Params
import javax.inject.Inject

class ObserveShowViewStats @Inject constructor(
    private val repository: FollowedShowsRepository
) : SubjectInteractor<Params, FollowedShowsWatchStats?>() {

    override fun createObservable(params: Params) =
        repository.observeShowViewStats(params.showId)

    data class Params(val showId: Long)
}