package com.tanya.domain.observers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tanya.data.daos.PopularDao
import com.tanya.data.results.PopularEntryWithShow
import com.tanya.domain.PagingInteractor
import com.tanya.domain.interactors.UpdatePopularShows
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePagedPopularShows @Inject constructor(
    private val dao: PopularDao,
    private val updatePopularShows: UpdatePopularShows
) : PagingInteractor<ObservePagedPopularShows.Params, PopularEntryWithShow>() {

    override fun createObservable(params: Params) =
        Pager(
            config = params.pagingConfig,
            remoteMediator =
        )

    data class Params(
        override val pagingConfig: PagingConfig
        ) : PagingInteractor.Params<PopularEntryWithShow>

}