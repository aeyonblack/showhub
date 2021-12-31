package com.tanya.domain.observers

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.tanya.data.daos.TrendingDao
import com.tanya.data.results.TrendingEntryWithShow
import com.tanya.domain.PaginatedEntryRemoteMediator
import com.tanya.domain.PagingInteractor
import com.tanya.domain.interactors.UpdateTrendingShows
import javax.inject.Inject

class ObservePagedTrendingShows @Inject constructor(
    private val dao: TrendingDao,
    private val updateTrendingShows: UpdateTrendingShows
) : PagingInteractor<ObservePagedTrendingShows.Params, TrendingEntryWithShow>() {

    @OptIn(ExperimentalPagingApi::class)
    override fun createObservable(params: Params) =
        Pager(
            config = params.pagingConfig,
            remoteMediator = PaginatedEntryRemoteMediator {
                updateTrendingShows(
                    UpdateTrendingShows.Params(page = it, forceRefresh = true)
                )
            },
            pagingSourceFactory = dao::entriesPagingSource,
        ).flow

    data class Params(
        override val pagingConfig: PagingConfig
    ) : PagingInteractor.Params<TrendingEntryWithShow>
}