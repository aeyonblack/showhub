package com.tanya.data.mappers

import com.tanya.data.entities.ShowEntity
import com.uwetrottmann.tmdb2.entities.TvShow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TODO - FIND A WAY TO ADD THE IMDB ID LATER
 */
@Singleton
class TmdbShowToShow @Inject constructor() : Mapper<TvShow, ShowEntity> {
    override suspend fun map(from: TvShow) = ShowEntity(
        tmdbId = from.id,
        title = from.name,
        summary = from.overview,
        homepage = from.homepage,
        network = from.networks?.firstOrNull()?.name,
        networkLogoPath = from.networks?.firstOrNull()?.logo_path
    )
}