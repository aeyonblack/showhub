package com.tanya.data.mappers

import com.tanya.data.entities.EpisodeWatchEntity
import com.uwetrottmann.trakt5.entities.HistoryEntry
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Singleton
class TraktHistoryItemToEpisodeWatchEntity
@Inject constructor(): Mapper<HistoryEntry, EpisodeWatchEntity> {
    override suspend fun map(from: HistoryEntry) = EpisodeWatchEntity(
        episodeId = 0,
        traktId = from.id,
        watchedAt = from.watched_at
    )
}