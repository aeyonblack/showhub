package com.tanya.domain.observers

import com.tanya.data.repositories.followedshows.FollowedShowsRepository
import com.tanya.domain.SubjectInteractor
import com.tanya.domain.observers.ObserveShowFollowStatus.Params
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveShowFollowStatus @Inject constructor(
    private val repository: FollowedShowsRepository
) : SubjectInteractor<Params, Boolean>() {

   /* override fun createObservable(params: Params) =
        repository.observeIsShowFollowed(params.showId)*/

    override fun createObservable(params: Params): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    data class Params(val showId: Long)
}