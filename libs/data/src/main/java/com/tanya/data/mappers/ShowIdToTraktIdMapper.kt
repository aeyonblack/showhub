package com.tanya.data.mappers

import com.tanya.data.daos.ShowDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowIdToTraktIdMapper @Inject constructor(
    private val showDao: ShowDao
): Mapper<Long, Int?> {
    override suspend fun map(from: Long) =
        showDao.getTraktIdForShowId(from)
}