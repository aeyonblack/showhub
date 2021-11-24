package com.tanya.data.mappers

import com.tanya.data.entities.EpisodeEntity
import javax.inject.Inject
import javax.inject.Singleton
import com.uwetrottmann.trakt5.entities.Episode as TraktEpisode

@Singleton
class TraktEpisodeToEpisodeEntity
@Inject
constructor(): Mapper<TraktEpisode, EpisodeEntity> {
    override suspend fun map(from: TraktEpisode) = EpisodeEntity(
        seasonId = 0,
        traktId = from.ids?.trakt,
        tmdbId = from.ids?.tmdb,
        title = from.title,
        number = from.number,
        summary = from.overview,
        firstAired = from.first_aired,
        traktRating = from.rating?.toFloat() ?: 0f,
        traktRatingVotes = from.votes
    )
}