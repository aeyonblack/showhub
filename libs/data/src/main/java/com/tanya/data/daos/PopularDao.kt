package com.tanya.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.tanya.data.entities.PopularShowEntity
import com.tanya.data.results.PopularEntryWithShow
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PopularDao : PaginatedEntryDao<PopularShowEntity, PopularEntryWithShow>() {

    @Transaction
    @Query("SELECT * FROM popular_shows WHERE page = :page ORDER BY page_order")
    abstract fun entriesObservable(page: Int): Flow<List<PopularShowEntity>>

    @Transaction
    @Query("SELECT * FROM popular_shows " +
            "ORDER BY page, page_order LIMIT :count OFFSET :offset")
    abstract fun entriesObservable(count: Int, offset: Int): Flow<List<PopularEntryWithShow>>

    @Transaction
    @Query("SELECT * FROM popular_shows ORDER BY page, page_order")
    abstract fun entriesPagingSource(): PagingSource<Int, PopularEntryWithShow>

    @Query("DELETE FROM popular_shows WHERE page = :page")
    abstract override suspend fun deletePage(page: Int)

    @Query("DELETE FROM popular_shows")
    abstract override suspend fun deleteAll()

    @Query("SELECT MAX(page) FROM popular_shows")
    abstract override suspend fun getLastPage(): Int?
}