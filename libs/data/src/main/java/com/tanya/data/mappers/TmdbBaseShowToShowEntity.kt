package com.tanya.data.mappers

import com.tanya.data.entities.ShowEntity
import com.uwetrottmann.tmdb2.entities.BaseTvShow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Map [BaseTvShow] from the Tmdb library to our local [ShowEntity]
 */
@Singleton
class TmdbBaseShowToShowEntity @Inject constructor() : Mapper<BaseTvShow, ShowEntity> {
    override suspend fun map(from: BaseTvShow) = ShowEntity(
        tmdbId = from.id,
        title = from.name,
        summary = from.overview
    )
}