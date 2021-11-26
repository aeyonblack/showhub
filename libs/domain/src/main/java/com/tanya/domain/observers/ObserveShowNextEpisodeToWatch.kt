package com.tanya.domain.observers

import com.tanya.data.repositories.episodes.SeasonsEpisodesRepository
import com.tanya.data.results.EpisodeWithSeason
import com.tanya.domain.SubjectInteractor
import javax.inject.Inject

class ObserveShowNextEpisodeToWatch @Inject constructor(
    private val repository: SeasonsEpisodesRepository
) : SubjectInteractor<ObserveShowNextEpisodeToWatch.Params, EpisodeWithSeason?>() {

    override fun createObservable(params: Params) =
        repository.observeNextEpisodeToWatch(params.showId)

    data class Params(val showId: Long)
}