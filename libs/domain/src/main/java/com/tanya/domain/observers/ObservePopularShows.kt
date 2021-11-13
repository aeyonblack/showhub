package com.tanya.domain.observers

import com.tanya.data.daos.PopularDao
import com.tanya.data.results.PopularEntryWithShow
import com.tanya.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePopularShows @Inject constructor(
    private val popularShowsRepository: PopularDao
) : SubjectInteractor<ObservePopularShows.Params, List<PopularEntryWithShow>>() {

    override fun createObservable(
        params: Params
    ): Flow<List<PopularEntryWithShow>> =
        popularShowsRepository.entriesObservable(params.count, 0)
    
    data class Params(val count: Int  = 20)
}