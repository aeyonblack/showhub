package com.tanya.data.mappers

import com.tanya.data.entities.ShowEntity
import com.uwetrottmann.trakt5.entities.TrendingShow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Map a Trakt Trending [Show] from the Trakt library to our local [ShowEntity]
 */
@Singleton
class TraktTrendingShowToShow @Inject constructor(
    private val showMapper: TraktShowToShow
) : Mapper<TrendingShow, ShowEntity> {
    override suspend fun map(from: TrendingShow) = showMapper.map(from.show!!)
}