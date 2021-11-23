package com.tanya.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.tanya.data.entities.RelatedShowEntity
import com.tanya.data.results.RelatedShowEntryWithShow
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RelatedShowsDao : PairEntryDao<RelatedShowEntity, RelatedShowEntryWithShow>() {

    @Transaction
    @Query("SELECT * FROM related_shows WHERE show_id = :showId ORDER BY order_index")
    abstract override fun entries(showId: Long): List<RelatedShowEntity>

    @Transaction
    @Query("SELECT * FROM related_shows WHERE show_id = :showId ORDER BY order_index")
    abstract override fun entriesWithShows(showId: Long): List<RelatedShowEntryWithShow>

    @Transaction
    @Query("SELECT * FROM related_shows WHERE show_id = :showId ORDER BY order_index")
    abstract override fun entriesWithShowsObservable(showId: Long): Flow<List<RelatedShowEntryWithShow>>

    @Transaction
    @Query("SELECT * FROM related_shows WHERE show_id = :showId ORDER BY order_index")
    abstract fun entriesObservable(showId: Long): Flow<List<RelatedShowEntity>>

    @Query("DELETE FROM related_shows WHERE show_id = :showId")
    abstract override suspend fun deleteWithShowId(showId: Long)

    @Query("DELETE FROM related_shows")
    abstract suspend fun deleteAll()
}