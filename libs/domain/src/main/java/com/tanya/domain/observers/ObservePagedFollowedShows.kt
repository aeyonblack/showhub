package com.tanya.domain.observers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.tanya.data.entities.SortOption
import com.tanya.data.repositories.followedshows.FollowedShowsRepository
import com.tanya.data.results.FollowedShowEntryWithShow
import com.tanya.domain.PagingInteractor
import com.tanya.domain.observers.ObservePagedFollowedShows.Params
import javax.inject.Inject

class ObservePagedFollowedShows @Inject constructor(
    private val repository: FollowedShowsRepository
) : PagingInteractor<Params, FollowedShowEntryWithShow>() {

    override fun createObservable(params: Params) = Pager(config = params.pagingConfig) {
        repository.observeFollowedShows(params.sort, params.filter)
    }.flow

    data class Params(
        val filter: String? = null,
        val sort: SortOption,
        override val pagingConfig: PagingConfig
    ): PagingInteractor.Params<FollowedShowEntryWithShow>
}