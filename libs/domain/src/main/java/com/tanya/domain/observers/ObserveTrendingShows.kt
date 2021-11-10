package com.tanya.domain.observers

import com.tanya.data.daos.TrendingDao
import com.tanya.data.results.TrendingEntryWithShow
import com.tanya.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTrendingShows @Inject constructor(
    private val trendingShowsDao: TrendingDao
): SubjectInteractor<ObserveTrendingShows.Params, List<TrendingEntryWithShow>>() {

    override fun createObservable(params: Params): Flow<List<TrendingEntryWithShow>> =
        trendingShowsDao.entriesObservable(params.count, 0)

    data class Params(val count: Int = 20)
}