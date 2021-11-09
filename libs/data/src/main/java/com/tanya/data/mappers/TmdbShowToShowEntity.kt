package com.tanya.data.mappers

import com.tanya.data.entities.ShowEntity
import com.uwetrottmann.tmdb2.entities.TvShow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Map a Tmdb [TvShow] from the Tmdb API to our local [ShowEntity]
 */
@Singleton
class TmdbShowToShowEntity @Inject constructor() : Mapper<TvShow, ShowEntity> {
    override suspend fun map(from: TvShow) = ShowEntity(
        tmdbId = from.id,
        imdbId = from.external_ids?.imdb_id,
        title = from.name,
        summary = from.overview,
        homepage = from.homepage,
        network = from.networks?.firstOrNull()?.name,
        networkLogoPath = from.networks?.firstOrNull()?.logo_path
    )
}