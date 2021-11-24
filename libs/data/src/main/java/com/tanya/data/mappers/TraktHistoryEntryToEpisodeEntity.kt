package com.tanya.data.mappers

import com.tanya.data.entities.EpisodeEntity
import com.uwetrottmann.trakt5.entities.HistoryEntry
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Singleton
class TraktHistoryEntryToEpisodeEntity @Inject constructor(
    private val mapper: TraktEpisodeToEpisodeEntity
): Mapper<HistoryEntry, EpisodeEntity> {
    override suspend fun map(from: HistoryEntry) = mapper.map(from.episode)
}