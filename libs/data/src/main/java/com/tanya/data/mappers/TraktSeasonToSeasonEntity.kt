package com.tanya.data.mappers

import com.tanya.data.entities.SeasonEntity
import com.uwetrottmann.trakt5.entities.Season
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraktSeasonToSeasonEntity @Inject constructor(): Mapper<Season, SeasonEntity> {
    override suspend fun map(from: Season) = SeasonEntity(
        showId = 0,
        traktId = from.ids?.trakt,
        tmdbId = from.ids?.tmdb,
        number = from.number,
        title = from.title,
        summary = from.overview,
        traktRating = from.rating?.toFloat() ?: 0f,
        traktRatingVotes = from.votes ?: 0,
        episodeCount = from.episode_count,
        episodesAired = from.aired_episodes,
        network = from.network
    )
}