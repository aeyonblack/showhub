package com.tanya.data.mappers

import com.tanya.data.daos.SeasonsDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeasonIdToTraktIdMapper @Inject constructor(
    private val dao: SeasonsDao
): Mapper<Long, Int> {
    override suspend fun map(from: Long) = dao.traktIdForId(from)
        ?: throw IllegalArgumentException("Trakt ID for season with ID $from does not exist")
}