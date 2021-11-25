package com.tanya.data.repositories.followedshows

import androidx.paging.PagingSource
import com.tanya.data.DatabaseTransactionRunner
import com.tanya.data.daos.FollowedShowsDao
import com.tanya.data.entities.FollowedShowEntity
import com.tanya.data.entities.PendingAction
import com.tanya.data.entities.SortOption
import com.tanya.data.results.FollowedShowEntryWithShow
import com.tanya.data.syncers.syncerForEntity
import com.tanya.data.views.FollowedShowsWatchStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FollowedShowsStore @Inject constructor(
    private val transactionRunner: DatabaseTransactionRunner,
    private val followedShowsDao: FollowedShowsDao,
) {
    var traktListId: Int? = null

    private val syncer = syncerForEntity(
        followedShowsDao,
        { it.traktId },
        { entity, id -> entity.copy(id = id ?: 0) },
    )

    suspend fun getEntryForShowId(showId: Long): FollowedShowEntity? = followedShowsDao.entryWithShowId(showId)

    suspend fun getEntries(): List<FollowedShowEntity> = followedShowsDao.entries()

    suspend fun getEntriesWithAddAction() = followedShowsDao.entriesWithSendPendingActions()

    suspend fun getEntriesWithDeleteAction() = followedShowsDao.entriesWithDeletePendingActions()

    suspend fun updateEntriesWithAction(ids: List<Long>, action: PendingAction): Int {
        return followedShowsDao.updateEntriesToPendingAction(ids, action.value)
    }

    suspend fun deleteEntriesInIds(ids: List<Long>) = followedShowsDao.deleteWithIds(ids)

    fun observeForPaging(
        sort: SortOption,
        filter: String?
    ): PagingSource<Int, FollowedShowEntryWithShow> {
        val filtered = filter != null && filter.isNotEmpty()
        return when (sort) {
            SortOption.SUPER_SORT -> {
                if (filtered) {
                    followedShowsDao.pagedListSuperSortFilter("*$filter*")
                } else {
                    followedShowsDao.pagedListSuperSort()
                }
            }
            SortOption.LAST_WATCHED -> {
                if (filtered) {
                    followedShowsDao.pagedListLastWatchedFilter("*$filter*")
                } else {
                    followedShowsDao.pagedListLastWatched()
                }
            }
            SortOption.ALPHABETICAL -> {
                if (filtered) {
                    followedShowsDao.pagedListAlphaFilter("*$filter*")
                } else {
                    followedShowsDao.pagedListAlpha()
                }
            }
            SortOption.DATE_ADDED -> {
                if (filtered) {
                    followedShowsDao.pagedListAddedFilter("*$filter*")
                } else {
                    followedShowsDao.pagedListAdded()
                }
            }
        }
    }

    fun observeIsShowFollowed(showId: Long): Flow<Boolean> {
        return followedShowsDao.entryCountWithShowIdNotPendingDeleteObservable(showId)
            .map { it > 0 }
    }

    fun observeNextShowToWatch(): Flow<FollowedShowEntryWithShow?> {
        return followedShowsDao.observeNextShowToWatch()
    }

    fun observeShowViewStats(showId: Long): Flow<FollowedShowsWatchStats?> {
        return followedShowsDao.entryShowViewStats(showId)
    }

    suspend fun isShowFollowed(showId: Long) = followedShowsDao.entryCountWithShowId(showId) > 0

    suspend fun sync(entities: List<FollowedShowEntity>) = transactionRunner {
        syncer.sync(followedShowsDao.entries(), entities)
    }

    suspend fun save(entry: FollowedShowEntity) = followedShowsDao.insertOrUpdate(entry)
}
