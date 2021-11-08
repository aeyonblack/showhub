package com.tanya.data.mappers

import com.tanya.data.entities.ShowEntity
import com.uwetrottmann.trakt5.entities.Show
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Map A Trakt [Show] from the trakt5 API to our local [ShowEntity]
 */
@Singleton
class TraktShowToShow @Inject constructor(
) : Mapper<Show, ShowEntity> {
    override suspend fun map(from: Show) = ShowEntity(
        traktId = from.ids?.trakt,
        tmdbId = from.ids?.tmdb,
        imdbId = from.ids?.imdb,
        title = from.title,
        summary = from.overview,
        homepage = from.homepage,
        traktRating = from.rating?.toFloat(),
        traktVotes = from.votes,
        certification = from.certification,
        runtime = from.runtime,
        network = from.network,
        country = from.country,
    )
}