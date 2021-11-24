package com.tanya.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.tanya.data.entities.FollowedShowEntity
import com.tanya.data.entities.PendingAction
import com.tanya.data.entities.SeasonEntity
import com.tanya.data.results.FollowedShowEntryWithShow
import com.tanya.data.views.FollowedShowsWatchStats
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FollowedShowsDao : EntryDao<FollowedShowEntity, FollowedShowEntryWithShow>() {
    @Query("SELECT * FROM library")
    abstract suspend fun entries(): List<FollowedShowEntity>

    @Transaction
    @Query(ENTRY_QUERY_SUPER_SORT)
    internal abstract fun pagedListSuperSort(): PagingSource<Int, FollowedShowEntryWithShow>

    @Transaction
    @Query(ENTRY_QUERY_SUPER_SORT_FILTER)
    internal abstract fun pagedListSuperSortFilter(filter: String): PagingSource<Int, FollowedShowEntryWithShow>

    @Transaction
    @Query(ENTRY_QUERY_ORDER_LAST_WATCHED)
    internal abstract fun pagedListLastWatched(): PagingSource<Int, FollowedShowEntryWithShow>

    @Transaction
    @Query(ENTRY_QUERY_ORDER_LAST_WATCHED_FILTER)
    internal abstract fun pagedListLastWatchedFilter(filter: String): PagingSource<Int, FollowedShowEntryWithShow>

    @Transaction
    @Query(ENTRY_QUERY_ORDER_ALPHA)
    internal abstract fun pagedListAlpha(): PagingSource<Int, FollowedShowEntryWithShow>

    @Transaction
    @Query(ENTRY_QUERY_ORDER_ALPHA_FILTER)
    internal abstract fun pagedListAlphaFilter(filter: String): PagingSource<Int, FollowedShowEntryWithShow>

    @Transaction
    @Query(ENTRY_QUERY_ORDER_ADDED)
    internal abstract fun pagedListAdded(): PagingSource<Int, FollowedShowEntryWithShow>

    @Transaction
    @Query(ENTRY_QUERY_ORDER_ADDED_FILTER)
    internal abstract fun pagedListAddedFilter(filter: String): PagingSource<Int, FollowedShowEntryWithShow>

    @Transaction
    @Query(
        """
        SELECT library.* FROM library
            INNER JOIN seasons AS s ON s.show_id = library.show_id
			INNER JOIN followed_next_to_watch AS next ON next.id = library.id
			INNER JOIN episodes AS eps ON eps.season_id = s.id
            INNER JOIN watched_episodes AS w ON w.episode_id = eps.id
            WHERE s.number != ${SeasonEntity.NUMBER_SPECIALS} AND s.ignored = 0
			ORDER BY datetime(w.watched_at) DESC
			LIMIT 1
    """
    )
    abstract fun observeNextShowToWatch(): Flow<FollowedShowEntryWithShow?>

    @Query("DELETE FROM library")
    abstract override suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM library WHERE id = :id")
    abstract suspend fun entryWithId(id: Long): FollowedShowEntryWithShow?

    @Query("SELECT * FROM library WHERE show_id = :showId")
    abstract suspend fun entryWithShowId(showId: Long): FollowedShowEntity?

    @Query("SELECT COUNT(*) FROM library WHERE show_id = :showId AND pending_action != 'delete'")
    abstract fun entryCountWithShowIdNotPendingDeleteObservable(showId: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM library WHERE show_id = :showId")
    abstract suspend fun entryCountWithShowId(showId: Long): Int

    @Transaction
    @Query(
        """
        SELECT stats.* FROM FollowedShowsWatchStats as stats
        INNER JOIN library ON stats.id = library.id
        WHERE show_id = :showId
    """
    )
    abstract fun entryShowViewStats(showId: Long): Flow<FollowedShowsWatchStats>

    suspend fun entriesWithSendPendingActions() = entriesWithPendingAction(PendingAction.UPLOAD.value)

    suspend fun entriesWithDeletePendingActions() = entriesWithPendingAction(PendingAction.DELETE.value)

    @Query("SELECT * FROM library WHERE pending_action = :pendingAction")
    internal abstract suspend fun entriesWithPendingAction(pendingAction: String): List<FollowedShowEntity>

    @Query("UPDATE library SET pending_action = :pendingAction WHERE id IN (:ids)")
    abstract suspend fun updateEntriesToPendingAction(ids: List<Long>, pendingAction: String): Int

    @Query("DELETE FROM library WHERE id IN (:ids)")
    abstract suspend fun deleteWithIds(ids: List<Long>): Int

    companion object {
        private const val ENTRY_QUERY_SUPER_SORT =
            """
                SELECT fs.* FROM library as fs
                INNER JOIN seasons AS s ON fs.show_id = s.show_id
                INNER JOIN episodes AS eps ON eps.season_id = s.id
                LEFT JOIN watched_episodes as ew ON ew.episode_id = eps.id
                LEFT JOIN followed_next_to_watch as nw ON nw.id = fs.id
                WHERE s.number != ${SeasonEntity.NUMBER_SPECIALS}
                AND s.ignored = 0
                GROUP BY fs.id
                ORDER BY
                /* shows with aired episodes to watch first */
                SUM(CASE WHEN datetime(first_aired) < datetime('now') THEN 1 ELSE 0 END) = COUNT(watched_at) ASC,
                /* latest event */
                MAX(
                    MAX(datetime(coalesce(next_to_watch_air_date, 0))), /* next episode to watch */
                    MAX(datetime(coalesce(watched_at, 0))), /* last watch */
                    MAX(datetime(coalesce(followed_at, 0))) /* when followed */
                ) DESC
            """

        private const val ENTRY_QUERY_SUPER_SORT_FILTER =
            """
                SELECT fs.* FROM library as fs
                INNER JOIN shows_fts AS s_fts ON fs.show_id = s_fts.docid
                INNER JOIN seasons AS s ON fs.show_id = s.show_id
                INNER JOIN episodes AS eps ON eps.season_id = s.id
                LEFT JOIN watched_episodes as ew ON ew.episode_id = eps.id
                LEFT JOIN followed_next_to_watch as nw ON nw.id = fs.id
                WHERE s.number != ${SeasonEntity.NUMBER_SPECIALS}
                AND s.ignored = 0
                AND s_fts.title MATCH :filter
                GROUP BY fs.id
                ORDER BY
                /* shows with aired episodes to watch first */
                SUM(CASE WHEN datetime(first_aired) < datetime('now') THEN 1 ELSE 0 END) = COUNT(watched_at) ASC,
                /* latest event */
                MAX(
                    MAX(datetime(coalesce(next_to_watch_air_date, 0))), /* next episode to watch */
                    MAX(datetime(coalesce(watched_at, 0))), /* last watch */
                    MAX(datetime(coalesce(followed_at, 0))) /* when followed */
                ) DESC
            """

        private const val ENTRY_QUERY_ORDER_LAST_WATCHED =
            """
                SELECT fs.* FROM library as fs
                INNER JOIN seasons AS s ON fs.show_id = s.show_id
                INNER JOIN episodes AS eps ON eps.season_id = s.id
                LEFT JOIN watched_episodes as ew ON ew.episode_id = eps.id
                GROUP BY fs.id
                ORDER BY MAX(datetime(ew.watched_at)) DESC
            """

        private const val ENTRY_QUERY_ORDER_LAST_WATCHED_FILTER =
            """
                SELECT fs.* FROM library as fs
                INNER JOIN shows_fts AS s_fts ON fs.show_id = s_fts.docid
                INNER JOIN seasons AS s ON fs.show_id = s.show_id
                INNER JOIN episodes AS eps ON eps.season_id = s.id
                LEFT JOIN watched_episodes as ew ON ew.episode_id = eps.id
                WHERE s_fts.title MATCH :filter
                GROUP BY fs.id
                ORDER BY MAX(datetime(ew.watched_at)) DESC
            """

        private const val ENTRY_QUERY_ORDER_ALPHA =
            """
                SELECT fs.* FROM library as fs
                INNER JOIN shows_fts AS s_fts ON fs.show_id = s_fts.docid
                ORDER BY title ASC
            """

        private const val ENTRY_QUERY_ORDER_ALPHA_FILTER =
            """
                SELECT fs.* FROM library as fs
                INNER JOIN shows_fts AS s_fts ON fs.show_id = s_fts.docid
                WHERE s_fts.title MATCH :filter
                ORDER BY title ASC
            """

        private const val ENTRY_QUERY_ORDER_ADDED =
            """
                SELECT * FROM library
                ORDER BY datetime(followed_at) DESC
            """

        private const val ENTRY_QUERY_ORDER_ADDED_FILTER =
            """
                SELECT fs.* FROM library as fs
                INNER JOIN shows_fts AS s_fts ON fs.show_id = s_fts.docid
                WHERE s_fts.title MATCH :filter
                ORDER BY datetime(followed_at) DESC
            """
    }
}

