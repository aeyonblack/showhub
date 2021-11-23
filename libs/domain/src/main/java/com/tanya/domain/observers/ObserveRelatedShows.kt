package com.tanya.domain.observers

import com.tanya.data.daos.RelatedShowsDao
import com.tanya.data.results.RelatedShowEntryWithShow
import com.tanya.domain.SubjectInteractor
import com.tanya.domain.observers.ObserveRelatedShows.Params
import javax.inject.Inject

class ObserveRelatedShows @Inject constructor(
    private val relatedShowsDao: RelatedShowsDao
): SubjectInteractor<Params, List<RelatedShowEntryWithShow>>() {

    override fun createObservable(params: Params) =
        relatedShowsDao.entriesWithShowsObservable(params.showId)

    data class Params(val showId: Long)
}