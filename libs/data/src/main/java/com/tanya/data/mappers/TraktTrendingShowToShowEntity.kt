package com.tanya.data.mappers

import com.tanya.data.entities.ShowEntity
import com.uwetrottmann.trakt5.entities.TrendingShow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Map a Trakt [TrendingShow] from the Trakt library to our local [ShowEntity]
 */
@Singleton
class TraktTrendingShowToShowEntity @Inject constructor(
    private val showEntityMapper: TraktShowToShowEntity
) : Mapper<TrendingShow, ShowEntity> {
    override suspend fun map(from: TrendingShow) = showEntityMapper.map(from.show!!)
}