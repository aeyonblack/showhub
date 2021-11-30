package com.tanya.data.repositories.episodes

import com.tanya.base.extensions.executeWithRetry
import com.tanya.base.extensions.toResult
import com.tanya.data.mappers.ShowIdToTmdbIdMapper
import com.tanya.data.mappers.TmdbSeasonToSeasonEntity
import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject

class TmdbSeasonDataSource @Inject constructor(
    private val tmdbIdMapper: ShowIdToTmdbIdMapper,
    private val tmdb: Tmdb,
    private val mapper: TmdbSeasonToSeasonEntity
) {
    suspend fun getSeason(
        showId: Long,
        seasonNumber: Int
    ) = tmdb
        .tvSeasonsService()
        .season(tmdbIdMapper.map(showId), seasonNumber, null)
        .executeWithRetry()
        .toResult(mapper::map)
}