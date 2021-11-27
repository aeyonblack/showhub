package com.tanya.data.repositories.followedshows

import com.tanya.base.data.entities.Success
import com.tanya.data.daos.ShowDao
import com.tanya.data.entities.FollowedShowEntity
import com.tanya.data.entities.PendingAction
import com.tanya.data.entities.SortOption
import com.tanya.data.syncers.ItemSyncerResult
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FollowedShowsRepository @Inject constructor(
    private val followedShowsStore: FollowedShowsStore,
    private val dataSource: TraktFollowedShowsDataSource,
    //private val traktAuthState: Provider<TraktAuthState>,
    private val showDao: ShowDao
) {
    fun observeFollowedShows(
        sort: SortOption,
        filter: String? = null
    ) = followedShowsStore.observeForPaging(sort, filter)

    fun observeShowViewStats(showId: Long) = followedShowsStore.observeShowViewStats(showId)

    fun observeIsShowFollowed(showId: Long) = followedShowsStore.observeIsShowFollowed(showId)

    /*fun observeNextShowToWatch() = followedShowsStore.observeNextShowToWatch()*/

    suspend fun isShowFollowed(showId: Long) = followedShowsStore.isShowFollowed(showId)

    suspend fun getFollowedShows(): List<FollowedShowEntity> {
        return followedShowsStore.getEntries()
    }

    /*suspend fun needFollowedShowsSync(*//**//*expiry: Instant = instantInPast(hours = 1)*//**//*): Boolean {
        return true *//**//*followedShowsLastRequestStore.isRequestBefore(expiry)*//**//*
    }*/

    suspend fun addFollowedShow(showId: Long) {
        val entry = followedShowsStore.getEntryForShowId(showId)

        if (entry == null || entry.pendingAction == PendingAction.DELETE) {
            // If we don't have an entry, or it is marked for deletion, lets update it to be uploaded
            val newEntry = FollowedShowEntity(
                id = entry?.id ?: 0,
                showId = showId,
                followedAt = entry?.followedAt ?: OffsetDateTime.now(),
                pendingAction = PendingAction.UPLOAD
            )
            followedShowsStore.save(newEntry)
            //val newEntryId = followedShowsStore.save(newEntry)
        }
    }

    suspend fun removeFollowedShow(showId: Long) {
        // Update the followed show to be deleted
        val entry = followedShowsStore.getEntryForShowId(showId)
        if (entry != null) {
            // Mark the show as pending deletion
            followedShowsStore.save(entry.copy(pendingAction = PendingAction.DELETE))
        }
    }

    suspend fun syncFollowedShows(): ItemSyncerResult<FollowedShowEntity> {
        /*val listId = when (traktAuthState.get()) {
            TraktAuthState.LOGGED_IN -> getFollowedTraktListId()
            else -> null
        }*/
        val listId = null

        processPendingAdditions(listId)
        processPendingDelete(listId)

        /*return when {
            listId != null -> pullDownTraktFollowedList(listId)
            else -> ItemSyncerResult()
        }.also {
            followedShowsLastRequestStore.updateLastRequest()
        }*/
        return ItemSyncerResult()
    }

    private suspend fun pullDownTraktFollowedList(
        listId: Int
    ): ItemSyncerResult<FollowedShowEntity> {
        val response = dataSource.getListShows(listId)
        return response.getOrThrow().map { (entry, show) ->
            // Grab the show id if it exists, or save the show and use it's generated ID
            val showId = showDao.getIdOrSavePlaceholder(show)
            // Create a followed show entry with the show id
            entry.copy(showId = showId)
        }.let { entries ->
            // Save the show entries
            followedShowsStore.sync(entries)
        }
    }

    private suspend fun processPendingAdditions(listId: Int?) {
        val pending = followedShowsStore.getEntriesWithAddAction()

        if (pending.isEmpty()) {
            return
        }

        /*if (listId != null && traktAuthState.get() == TraktAuthState.LOGGED_IN) {
            val shows = pending.mapNotNull { showDao.getShowWithId(it.showId) }

            val response = dataSource.addShowIdsToList(listId, shows)

            if (response is Success) {
                // Now update the database
                followedShowsStore.updateEntriesWithAction(pending.map { it.id }, PendingAction.NOTHING)
            }
        } else {
            // We're not logged in, so just update the database
            followedShowsStore.updateEntriesWithAction(pending.map { it.id }, PendingAction.NOTHING)
        }*/
        followedShowsStore.updateEntriesWithAction(pending.map { it.id }, PendingAction.NOTHING)
    }

    private suspend fun processPendingDelete(listId: Int?) {
        val pending = followedShowsStore.getEntriesWithDeleteAction()

        if (pending.isEmpty()) {
            return
        }

        /*if (listId != null && traktAuthState.get() == TraktAuthState.LOGGED_IN) {
            val shows = pending.mapNotNull { showDao.getShowWithId(it.showId) }

            val response = dataSource.removeShowIdsFromList(listId, shows)

            if (response is Success) {
                // Now update the database
                followedShowsStore.deleteEntriesInIds(pending.map { it.id })
            }
        } else {
            // We're not logged in, so just update the database
            followedShowsStore.deleteEntriesInIds(pending.map { it.id })
        }*/
        followedShowsStore.deleteEntriesInIds(pending.map { it.id })
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
