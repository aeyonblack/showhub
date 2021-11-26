package com.tanya.domain.observers

import com.tanya.data.repositories.episodes.SeasonsEpisodesRepository
import com.tanya.data.results.SeasonWithEpisodesAndWatches
import com.tanya.domain.SubjectInteractor
import com.tanya.domain.observers.ObserveShowSeasonsEpisodesWatches.Params
import javax.inject.Inject

class ObserveShowSeasonsEpisodesWatches @Inject constructor(
    private val repository: SeasonsEpisodesRepository
) :  SubjectInteractor<Params, List<SeasonWithEpisodesAndWatches>>() {

    override fun createObservable(params: Params) =
        repository.observeSeasonsWithEpisodesWatchedForShow(params.showId)

    data class Params(val showId: Long)
}