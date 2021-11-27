package com.tanya.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.tanya.data.entities.EpisodeEntity
import com.tanya.data.entities.SeasonEntity
import com.tanya.data.results.EpisodeWithSeason
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EpisodesDao : EntityDao<EpisodeEntity>() {
    @Query("SELECT * from episodes WHERE season_id = :seasonId ORDER BY number")
    abstract suspend fun episodesWithSeasonId(seasonId: Long): List<EpisodeEntity>

    @Query("DELETE FROM episodes WHERE season_id = :seasonId")
    abstract suspend fun deleteWithSeasonId(seasonId: Long)

    @Query("SELECT * from episodes WHERE trakt_id = :traktId")
    abstract suspend fun episodeWithTraktId(traktId: Int): EpisodeEntity?

    @Query("SELECT * from episodes WHERE tmdb_id = :tmdbId")
    abstract suspend fun episodeWithTmdbId(tmdbId: Int): EpisodeEntity?

    @Query("SELECT * from episodes WHERE id = :id")
    abstract suspend fun episodeWithId(id: Long): EpisodeEntity?

    @Query("SELECT trakt_id from episodes WHERE id = :id")
    abstract suspend fun episodeTraktIdForId(id: Long): Int?

    @Query("SELECT id from episodes WHERE trakt_id = :traktId")
    abstract suspend fun episodeIdWithTraktId(traktId: Int): Long?

    @Transaction
    @Query("SELECT * from episodes WHERE id = :id")
    abstract fun episodeWithIdObservable(id: Long): Flow<EpisodeWithSeason>

    @Query(
        "SELECT shows.id FROM shows" +
                " INNER JOIN seasons AS s ON s.show_id = shows.id" +
                " INNER JOIN episodes AS eps ON eps.season_id = s.id" +
                " WHERE eps.id = :episodeId"
    )
    abstract suspend fun showIdForEpisodeId(episodeId: Long): Long

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(latestWatchedEpisodeForShowId)
    abstract fun observeLatestWatchedEpisodeForShowId(showId: Long): Flow<EpisodeWithSeason?>

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(nextEpisodeForShowIdAfter)
    abstract fun observeNextEpisodeForShowAfter(
        showId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): Flow<EpisodeWithSeason?>

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(nextAiredEpisodeForShowIdAfter)
    abstract fun observeNextAiredEpisodeForShowAfter(
        showId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): Flow<EpisodeWithSeason?>

    companion object {
        const val latestWatchedEpisodeForShowId =
            """
                SELECT eps.*, (100 * s.number) + eps.number AS computed_abs_number
                FROM shows
                INNER JOIN seasons AS s ON shows.id = s.show_id
                INNER JOIN episodes AS eps ON eps.season_id = s.id
                INNER JOIN watched_episodes AS ew ON ew.episode_id = eps.id
                WHERE s.number != ${SeasonEntity.NUMBER_SPECIALS}
                AND s.ignored = 0
                AND shows.id = :showId
                ORDER BY computed_abs_number DESC
                LIMIT 1
            """

        const val nextEpisodeForShowIdAfter =
            """
                SELECT eps.*, (1000 * s.number) + eps.number AS computed_abs_number
                FROM shows
                INNER JOIN seasons AS s ON shows.id = s.show_id
                INNER JOIN episodes AS eps ON eps.season_id = s.id
                WHERE s.number != ${SeasonEntity.NUMBER_SPECIALS}
                AND s.ignored = 0
                AND shows.id = :showId
                AND computed_abs_number > ((1000 * :seasonNumber) + :episodeNumber)
                ORDER BY computed_abs_number ASC
                LIMIT 1
            """

        const val nextAiredEpisodeForShowIdAfter =
            """
                SELECT eps.*, (1000 * s.number) + eps.number AS computed_abs_number
                FROM shows
                INNER JOIN seasons AS s ON shows.id = s.show_id
                INNER JOIN episodes AS eps ON eps.season_id = s.id
                WHERE s.number != ${SeasonEntity.NUMBER_SPECIALS}
                AND s.ignored = 0
                AND shows.id = :showId
                AND computed_abs_number > ((1000 * :seasonNumber) + :episodeNumber)
                AND eps.first_aired IS NOT NULL
                AND datetime(eps.first_aired) < datetime('now')
                ORDER BY computed_abs_number ASC
                LIMIT 1
            """
    }
}
