package com.tanya.data.repositories.followedshows

import com.tanya.base.data.entities.Success
import com.tanya.base.util.Logger
import com.tanya.data.daos.ShowDao
import com.tanya.data.entities.FollowedShowEntity
import com.tanya.data.entities.PendingAction
import com.tanya.data.entities.SortOption
import com.tanya.data.extensions.instantInPast
import com.tanya.data.syncers.ItemSyncerResult
import com.tanya.trakt.TraktAuthState
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class FollowedShowsRepository @Inject constructor(
    private val followedShowsStore: FollowedShowsStore,
    private val dataSource: TraktFollowedShowsDataSource,
    private val traktAuthState: Provider<TraktAuthState>,
    private val showDao: ShowDao,
    private val logger: Logger
) {
    fun observeFollowedShows(
        sort: SortOption,
        filter: String? = null
    ) = followedShowsStore.observeForPaging(sort, filter)

    fun observeShowViewStats(showId: Long) = followedShowsStore.observeShowViewStats(showId)

    fun observeIsShowFollowed(showId: Long) = followedShowsStore.observeIsShowFollowed(showId)

    fun observeNextShowToWatch() = followedShowsStore.observeNextShowToWatch()

    suspend fun isShowFollowed(showId: Long) = followedShowsStore.isShowFollowed(showId)

    suspend fun getFollowedShows(): List<FollowedShowEntity> {
        return followedShowsStore.getEntries()
    }

    suspend fun needFollowedShowsSync(expiry: Instant = instantInPast(hours = 1)): Boolean {
        return true /*followedShowsLastRequestStore.isRequestBefore(expiry)*/
    }

    suspend fun addFollowedShow(showId: Long) {
        logger.d("addFollowedShow: Start of Function")
        val entry = followedShowsStore.getEntryForShowId(showId)
        logger.d("addFollowedShow: Mid of Function")
        if (entry == null || entry.pendingAction == PendingAction.DELETE) {
            logger.d("addFollowedShow: Creating new entry")
            try {
                val newEntry = FollowedShowEntity(
                    id = entry?.id ?: 0,
                    showId = showId,
                    followedAt = entry?.followedAt ?: OffsetDateTime.now(),
                    pendingAction = PendingAction.UPLOAD
                )
                followedShowsStore.save(newEntry)
            } catch (t: Throwable) {
                logger.d("Something happened while creating entry: ${t.message}")
            }
            logger.d("addFollowedShow: New entry created")


            logger.d("addFollowedShow: Entry saved")
        }
        logger.d("addFollowedShow: End of Function")
    }

    suspend fun removeFollowedShow(showId: Long) {
        val entry = followedShowsStore.getEntryForShowId(showId)
        if (entry != null) {
            followedShowsStore.save(entry.copy(pendingAction = PendingAction.DELETE))
        }
    }

    suspend fun syncFollowedShows(): ItemSyncerResult<FollowedShowEntity> {
        val listId = when (traktAuthState.get()) {
            TraktAuthState.LOGGED_IN -> getFollowedTraktListId()
            else -> null
        }

        processPendingAdditions(listId)
        processPendingDelete(listId)

        return when {
            listId != null -> pullDownTraktFollowedList(listId)
            else -> ItemSyncerResult()
        }/*.also {
            followedShowsLastRequestStore.updateLastRequest()
        }*/
    }

    private suspend fun pullDownTraktFollowedList(
        listId: Int
    ): ItemSyncerResult<FollowedShowEntity> {
        val response = dataSource.getListShows(listId)
        return response.getOrThrow().map { (entry, show) ->
            val showId = showDao.getIdOrSavePlaceholder(show)
            entry.copy(showId = showId)
        }.let { entries ->
            followedShowsStore.sync(entries)
        }
    }

    private suspend fun processPendingAdditions(listId: Int?) {
        val pending = followedShowsStore.getEntriesWithAddAction()

        if (pending.isEmpty()) {
            return
        }

        if (listId != null && traktAuthState.get() == TraktAuthState.LOGGED_IN) {
            val shows = pending.mapNotNull { showDao.getShowWithId(it.showId) }

            val response = dataSource.addShowIdsToList(listId, shows)

            if (response is Success) {
                followedShowsStore.updateEntriesWithAction(pending.map { it.id }, PendingAction.NOTHING)
            }
        } else {
            followedShowsStore.updateEntriesWithAction(pending.map { it.id }, PendingAction.NOTHING)
        }
    }

    private suspend fun processPendingDelete(listId: Int?) {
        val pending = followedShowsStore.getEntriesWithDeleteAction()

        if (pending.isEmpty()) {
            return
        }

        if (listId != null && traktAuthState.get() == TraktAuthState.LOGGED_IN) {
            val shows = pending.mapNotNull { showDao.getShowWithId(it.showId) }

            val response = dataSource.removeShowIdsFromList(listId, shows)

            if (response is Success) {
                followedShowsStore.deleteEntriesInIds(pending.map { it.id })
            }
        } else {
            followedShowsStore.deleteEntriesInIds(pending.map { it.id })
        }
    }

    private suspend fun getFollowedTraktListId(): Int? {
        if (followedShowsStore.traktListId == null) {
            val result = dataSource.getFollowedListId()
            if (result is Success) {
                followedShowsStore.traktListId = result.get()
            }
        }
        return followedShowsStore.traktListId
    }
}
