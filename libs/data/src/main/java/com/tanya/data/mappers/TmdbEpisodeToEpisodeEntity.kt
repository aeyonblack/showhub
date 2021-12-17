package com.tanya.data.mappers

import com.tanya.data.entities.EpisodeEntity
import com.tanya.tmdb.TmdbImageUrlProvider
import com.uwetrottmann.tmdb2.entities.TvEpisode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbEpisodeToEpisodeEntity @Inject constructor(
    private val tmdbImageUrlProvider: TmdbImageUrlProvider
): Mapper<TvEpisode, EpisodeEntity> {
    override suspend fun map(from: TvEpisode) = EpisodeEntity(
        seasonId = 0,
        tmdbId = from.id,
        title = from.name,
        number = from.episode_number,
        summary = from.overview,
        tmdbBackdropPath = tmdbImageUrlProvider.getBackdropUrl(from.still_path!!, 1280)
    )
}