package com.tanya.data.repositories.episodes

import com.tanya.data.DatabaseTransactionRunner
import com.tanya.data.daos.EpisodeWatchEntityDao
import com.tanya.data.entities.EpisodeWatchEntity
import com.tanya.data.syncers.syncerForEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EpisodeWatchStore @Inject constructor(
    private val transactionRunner: DatabaseTransactionRunner,
    private val dao: EpisodeWatchEntityDao
) {

    private val episodeWatchSyncer = syncerForEntity(
        dao,
        {it.traktId},
        {entity, id -> entity.copy(id = id ?: 0)}
    )

    fun observeEpisodeWatches(episodeId: Long): Flow<List<EpisodeWatchEntity>> =
        dao.watchesForEpisodeObservable(episodeId)

}