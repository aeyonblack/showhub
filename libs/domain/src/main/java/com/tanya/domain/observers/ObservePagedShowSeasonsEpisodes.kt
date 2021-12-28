package com.tanya.domain.observers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.tanya.data.repositories.episodes.SeasonsEpisodesRepository
import com.tanya.data.results.SeasonWithEpisodesAndWatches
import com.tanya.domain.PagingInteractor
import javax.inject.Inject

class ObservePagedShowSeasonsEpisodes @Inject constructor(
    private val repository: SeasonsEpisodesRepository
) : PagingInteractor<ObservePagedShowSeasonsEpisodes.Params, SeasonWithEpisodesAndWatches>() {

    override fun createObservable(params: Params) = Pager(config = params.pagingConfig) {
        repository.observePagedSeasonsWithEpisodesWatchedForShow(params.showId)
    }.flow

    data class Params(
        val showId: Long,
        override val pagingConfig: PagingConfig
        ) : PagingInteractor.Params<SeasonWithEpisodesAndWatches>
}