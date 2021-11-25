package com.tanya.data.repositories.episodes

import com.tanya.data.DatabaseTransactionRunner
import com.tanya.data.daos.EpisodeWatchEntityDao
import com.tanya.data.entities.EpisodeWatchEntity
import com.tanya.data.entities.PendingAction
import com.tanya.data.syncers.syncerForEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EpisodeWatchStore @Inject constructor(
    private val transactionRunner: DatabaseTransactionRunner,
    private val episodeWatchEntryDao: EpisodeWatchEntityDao
) {

    private val episodeWatchSyncer = syncerForEntity(
        episodeWatchEntryDao,
        {it.traktId},
        {entity, id -> entity.copy(id = id ?: 0)}
    )

    fun observeEpisodeWatches(episodeId: Long): Flow<List<EpisodeWatchEntity>> =
        episodeWatchEntryDao.watchesForEpisodeObservable(episodeId)

    suspend fun save(watch: EpisodeWatchEntity) = episodeWatchEntryDao.insertOrUpdate(watch)

    suspend fun save(watches: List<EpisodeWatchEntity>) = episodeWatchEntryDao.insertOrUpdate(watches)

    suspend fun getEpisodeWatchesForShow(showId: Long) = episodeWatchEntryDao.entriesForShowId(showId)

    suspend fun getWatchesForEpisode(episodeId: Long) = episodeWatchEntryDao.watchesForEpisode(episodeId)

    suspend fun getEpisodeWatch(watchId: Long) = episodeWatchEntryDao.entryWithId(watchId)

    suspend fun hasEpisodeBeenWatched(episodeId: Long) = episodeWatchEntryDao.watchCountForEpisode(episodeId) > 0

    suspend fun getEntriesWithAddAction(showId: Long) = episodeWatchEntryDao.entriesForShowIdWithSendPendingActions(showId)

    suspend fun getEntriesWithDeleteAction(showId: Long) = episodeWatchEntryDao.entriesForShowIdWithDeletePendingActions(showId)

    suspend fun deleteEntriesWithIds(ids: List<Long>) = episodeWatchEntryDao.deleteWithIds(ids)

    suspend fun updateEntriesWithAction(ids: List<Long>, action: PendingAction): Int {
        return episodeWatchEntryDao.updateEntriesToPendingAction(ids, action.value)
    }

    suspend fun addNewShowWatchEntries(
        showId: Long,
        watches: List<EpisodeWatchEntity>
    ) = transactionRunner {
        val currentWatches = episodeWatchEntryDao.entriesForShowIdWithNoPendingAction(showId)
        episodeWatchSyncer.sync(currentWatches, watches, removeNotMatched = false)
    }

    suspend fun syncShowWatchEntries(
        showId: Long,
        watches: List<EpisodeWatchEntity>
    ) = transactionRunner {
        val currentWatches = episodeWatchEntryDao.entriesForShowIdWithNoPendingAction(showId)
        episodeWatchSyncer.sync(currentWatches, watches)
    }

    suspend fun syncEpisodeWatchEntries(
        episodeId: Long,
        watches: List<EpisodeWatchEntity>
    ) = transactionRunner {
        val currentWatches = episodeWatchEntryDao.watchesForEpisode(episodeId)
        episodeWatchSyncer.sync(currentWatches, watches)
    }
}