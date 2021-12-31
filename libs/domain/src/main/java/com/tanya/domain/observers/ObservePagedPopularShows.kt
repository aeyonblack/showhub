package com.tanya.domain.observers

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.tanya.data.daos.PopularDao
import com.tanya.data.results.PopularEntryWithShow
import com.tanya.domain.PaginatedEntryRemoteMediator
import com.tanya.domain.PagingInteractor
import com.tanya.domain.interactors.UpdatePopularShows
import javax.inject.Inject

class ObservePagedPopularShows @Inject constructor(
    private val dao: PopularDao,
    private val updatePopularShows: UpdatePopularShows
) : PagingInteractor<ObservePagedPopularShows.Params, PopularEntryWithShow>() {

    @OptIn(ExperimentalPagingApi::class)
    override fun createObservable(params: Params) =
        Pager(
            config = params.pagingConfig,
            remoteMediator = PaginatedEntryRemoteMediator {
                updatePopularShows.executeSync(
                    UpdatePopularShows.Params(page = it, forceRefresh = true)
                )
            },
            pagingSourceFactory = dao::entriesPagingSource
        ).flow

    data class Params(
        override val pagingConfig: PagingConfig
        ) : PagingInteractor.Params<PopularEntryWithShow>

}