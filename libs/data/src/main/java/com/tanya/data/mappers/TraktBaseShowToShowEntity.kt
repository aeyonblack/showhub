package com.tanya.data.mappers

import com.tanya.data.entities.ShowEntity
import com.uwetrottmann.trakt5.entities.BaseShow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Map [BaseShow] from Trakt library to our local [ShowEntity]
 */
@Singleton
class TraktBaseShowToShowEntity @Inject constructor(
    private val showEntityMapper: TraktShowToShowEntity
) : Mapper<BaseShow, ShowEntity>{
    override suspend fun map(from: BaseShow): ShowEntity {
        val mapped = showEntityMapper.map(from.show!!)
        return mapped.copy(
            traktDataUpdate = from.last_updated_at ?: mapped.traktDataUpdate
        )
    }
}