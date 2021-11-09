package com.tanya.data.mappers

import com.tanya.data.entities.TrendingShowEntity
import com.uwetrottmann.trakt5.entities.TrendingShow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Map [TrendingShow] from Trakt library to our local [TrendingShowEntity]
 */
@Singleton
class TraktTrendingShowToTrendingShowEntity
@Inject constructor() : Mapper<TrendingShow, TrendingShowEntity> {
    override suspend fun map(from: TrendingShow) = TrendingShowEntity(
        showId = 0,
        watchers = from.watchers ?: 0,
        page = 0
    )
}