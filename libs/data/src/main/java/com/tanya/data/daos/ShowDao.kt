package com.tanya.data.daos

import androidx.room.Dao
import androidx.room.Query
import com.tanya.data.entities.ShowEntity
import com.tanya.data.repositories.shows.mergeShows
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalArgumentException

@Dao
abstract class ShowDao : EntityDao<ShowEntity>() {

    @Query("SELECT * FROM shows WHERE trakt_id = :id")
    abstract suspend fun getShowWithTraktId(id: Int): ShowEntity?

    @Query("SELECT * FROM shows WHERE id IN(:ids)")
    abstract fun getShowsWithIds(ids: List<Long>): Flow<List<ShowEntity>>

    @Query("SELECT * FROM shows WHERE tmdb_id = :id")
    abstract suspend fun getShowWithTmdbId(id: Int): ShowEntity?

    @Query("SELECT * FROM shows WHERE id = :id")
    abstract fun getShowWithIdFlow(id: Long): Flow<ShowEntity>

    @Query("SELECT * FROM shows WHERE id = :id")
    abstract suspend fun getShowWithId(id: Long): ShowEntity?

    suspend fun getShowWithIdOrThrow(id: Long): ShowEntity =
        getShowWithId(id) ?: throw IllegalArgumentException("No show with id $id in database")

    @Query("SELECT trakt_id FROM shows WHERE id = :id")
    abstract suspend fun getTraktIdForShowId(id: Long): Int?

    @Query("SELECT tmdb_id FROM shows WHERE id = :id")
    abstract suspend fun getTmdbIdForShowId(id: Long): Int?

    @Query("SELECT id FROM shows WHERE trakt_id = :traktId")
    abstract suspend fun getIdForTraktId(traktId: Int): Long?

    @Query("SELECT id FROM shows WHERE tmdb_id = :tmdbId")
    abstract suspend fun getIdForTmdbId(tmdbId: Int): Long?

    @Query("DELETE FROM shows WHERE id = :id")
    abstract suspend fun delete(id: Long)

    @Query("DELETE FROM shows")
    abstract suspend fun deleteAll()

    suspend fun getIdOrSavePlaceholder(show: ShowEntity): Long {
        val idForTraktId: Long? = if (show.traktId != null) getIdForTraktId(show.traktId) else null
        val idForTmdbId: Long? = if (show.tmdbId != null) getIdForTmdbId(show.tmdbId) else null

        if (idForTraktId != null && idForTmdbId != null) {
            return if (idForTmdbId == idForTraktId) {
                // Great, the entities are matching
                idForTraktId
            } else {
                val showForTmdbId = getShowWithIdOrThrow(idForTmdbId)
                val showForTraktId = getShowWithIdOrThrow(idForTraktId)
                delete(showForTmdbId)
                return insertOrUpdate(mergeShows(showForTraktId, showForTraktId, showForTmdbId))
            }
        }

        if (idForTraktId != null) {
            // If we get here, we only have a entity with the trakt id
            return idForTraktId
        }
        if (idForTmdbId != null) {
            // If we get here, we only have a entity with the tmdb id
            return idForTmdbId
        }

        // TODO add fuzzy search on name or slug

        return insert(show)
    }
}