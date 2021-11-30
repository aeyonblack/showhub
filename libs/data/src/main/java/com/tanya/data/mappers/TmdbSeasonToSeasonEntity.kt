package com.tanya.data.mappers

import com.tanya.data.entities.SeasonEntity
import com.tanya.tmdb.TmdbImageUrlProvider
import com.uwetrottmann.tmdb2.entities.TvSeason
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbSeasonToSeasonEntity @Inject constructor(
    private val tmdbImageUrlProvider: TmdbImageUrlProvider
): Mapper<TvSeason, SeasonEntity> {
    override suspend fun map(from: TvSeason) = SeasonEntity(
        showId = 0,
        tmdbId = from.id,
        tmdbPosterPath = tmdbImageUrlProvider.getPosterUrl(from.poster_path!!, 780)
    )
}