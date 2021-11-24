package com.tanya.data.mappers

import com.tanya.data.daos.EpisodesDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpisodeIdToTraktIdMapper @Inject constructor(
    private val dao: EpisodesDao
): Mapper<Long, Int> {
    override suspend fun map(from: Long) = dao.episodeTraktIdForId(from)
        ?: throw IllegalArgumentException("Trakt ID for episode with ID $from does not exist")
}