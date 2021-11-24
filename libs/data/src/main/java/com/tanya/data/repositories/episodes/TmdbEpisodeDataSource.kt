package com.tanya.data.repositories.episodes

import com.tanya.base.extensions.executeWithRetry
import com.tanya.base.extensions.toResult
import com.tanya.data.mappers.ShowIdToTmdbIdMapper
import com.tanya.data.mappers.TmdbEpisodeToEpisodeEntity
import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject

class TmdbEpisodeDataSource @Inject constructor(
    private val tmdbIdMapper: ShowIdToTmdbIdMapper,
    private val tmdb: Tmdb,
    private val mapper: TmdbEpisodeToEpisodeEntity
): EpisodeDataSource {
    override suspend fun getEpisode(
        showId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ) = tmdb
        .tvEpisodesService()
        .episode(tmdbIdMapper.map(showId), seasonNumber, episodeNumber, null)
        .executeWithRetry()
        .toResult(mapper::map)
}