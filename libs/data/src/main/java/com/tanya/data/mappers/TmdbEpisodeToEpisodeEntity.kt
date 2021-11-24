package com.tanya.data.mappers

import com.tanya.data.entities.EpisodeEntity
import com.uwetrottmann.tmdb2.entities.TvEpisode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbEpisodeToEpisodeEntity @Inject constructor(): Mapper<TvEpisode, EpisodeEntity> {
    override suspend fun map(from: TvEpisode) = EpisodeEntity(
        seasonId = 0,
        tmdbId = from.id,
        title = from.name,
        number = from.episode_number,
        summary = from.overview,
        tmdbBackdropPath = from.still_path
    )
}