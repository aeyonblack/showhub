package com.tanya.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.tanya.data.entities.TrendingShowEntity
import com.tanya.data.results.TrendingEntryWithShow
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TrendingDao : PaginatedEntryDao<TrendingShowEntity, TrendingEntryWithShow>() {

    @Transaction
    @Query("SELECT * FROM trending_shows WHERE page = :page ORDER BY watchers DESC, id ASC")
    abstract fun entriesObservable(page: Int): Flow<List<TrendingShowEntity>>

    @Transaction
    @Query("SELECT * FROM trending_shows ORDER BY page ASC, watchers DESC, id ASC LIMIT :count OFFSET :offset")
    abstract fun entriesObservable(count: Int, offset: Int): Flow<List<TrendingEntryWithShow>>

    @Transaction
    @Query("SELECT * FROM trending_shows ORDER BY page ASC, watchers DESC, id ASC")
    abstract fun entriesPagingSource(): PagingSource<Int, TrendingEntryWithShow>

    @Query("DELETE FROM trending_shows WHERE page = :page")
    abstract override suspend fun deletePage(page: Int)

    @Query("DELETE FROM trending_shows")
    abstract override suspend fun deleteAll()

    @Query("SELECT MAX(page) FROM trending_shows")
    abstract override suspend fun getLastPage(): Int?

}