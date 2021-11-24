package com.tanya.data.daos

import androidx.room.Dao
import androidx.room.Query
import com.tanya.data.entities.EpisodeWatchEntity
import com.tanya.data.entities.PendingAction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EpisodeWatchEntityDao : EntityDao<EpisodeWatchEntity>() {
    @Query("SELECT * FROM watched_episodes WHERE episode_id = :episodeId")
    abstract suspend fun watchesForEpisode(episodeId: Long): List<EpisodeWatchEntity>

    @Query("SELECT COUNT(id) FROM watched_episodes WHERE episode_id = :episodeId")
    abstract suspend fun watchCountForEpisode(episodeId: Long): Int

    @Query("SELECT * FROM watched_episodes WHERE episode_id = :episodeId")
    abstract fun watchesForEpisodeObservable(episodeId: Long): Flow<List<EpisodeWatchEntity>>

    @Query("SELECT * FROM watched_episodes WHERE id = :id")
    abstract suspend fun entryWithId(id: Long): EpisodeWatchEntity?

    @Query("SELECT * FROM watched_episodes WHERE trakt_id = :traktId")
    abstract suspend fun entryWithTraktId(traktId: Long): EpisodeWatchEntity?

    @Query("SELECT id FROM watched_episodes WHERE trakt_id = :traktId")
    abstract suspend fun entryIdWithTraktId(traktId: Long): Long?

    suspend fun entriesForShowIdWithNoPendingAction(showId: Long): List<EpisodeWatchEntity> {
        return entriesForShowIdWithPendingAction(showId, PendingAction.NOTHING.value)
    }

    suspend fun entriesForShowIdWithSendPendingActions(showId: Long): List<EpisodeWatchEntity> {
        return entriesForShowIdWithPendingAction(showId, PendingAction.UPLOAD.value)
    }

    suspend fun entriesForShowIdWithDeletePendingActions(showId: Long): List<EpisodeWatchEntity> {
        return entriesForShowIdWithPendingAction(showId, PendingAction.DELETE.value)
    }

    @Query(
        """
        SELECT ew.* FROM watched_episodes AS ew
        INNER JOIN episodes AS eps ON ew.episode_id = eps.id
        INNER JOIN seasons AS s ON eps.season_id = s.id
        INNER JOIN shows ON s.show_id = shows.id
        WHERE shows.id = :showId AND ew.pending_action = :pendingAction
    """
    )
    internal abstract suspend fun entriesForShowIdWithPendingAction(
        showId: Long,
        pendingAction: String
    ): List<EpisodeWatchEntity>

    @Query(
        """
        SELECT ew.* FROM watched_episodes AS ew
        INNER JOIN episodes AS eps ON ew.episode_id = eps.id
        INNER JOIN seasons AS s ON eps.season_id = s.id
        INNER JOIN shows ON s.show_id = shows.id
        WHERE shows.id = :showId
    """
    )
    abstract suspend fun entriesForShowId(showId: Long): List<EpisodeWatchEntity>

    @Query("UPDATE watched_episodes SET pending_action = :pendingAction WHERE id IN (:ids)")
    abstract suspend fun updateEntriesToPendingAction(ids: List<Long>, pendingAction: String): Int

    @Query("DELETE FROM watched_episodes WHERE id = :id")
    abstract suspend fun deleteWithId(id: Long): Int

    @Query("DELETE FROM watched_episodes WHERE id IN (:ids)")
    abstract suspend fun deleteWithIds(ids: List<Long>): Int

    @Query("DELETE FROM watched_episodes WHERE trakt_id = :traktId")
    abstract suspend fun deleteWithTraktId(traktId: Long): Int
}