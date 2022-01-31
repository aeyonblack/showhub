package com.tanya.data.daos

import androidx.paging.PagingSource
import androidx.room.Query
import androidx.room.Transaction
import com.tanya.data.entities.RecommendedShowEntity
import com.tanya.data.results.RecommendedEntryWithShow
import kotlinx.coroutines.flow.Flow

abstract class RecommendedDao : PaginatedEntryDao<RecommendedShowEntity, RecommendedEntryWithShow>() {
    @Transaction
    @Query("SELECT * FROM recommended WHERE page = :page ORDER BY id ASC")
    abstract fun entriesForPage(page: Int): Flow<List<RecommendedShowEntity>>

    @Transaction
    @Query("SELECT * FROM recommended ORDER BY page ASC, id ASC LIMIT :count OFFSET :offset")
    abstract fun entriesObservable(count: Int, offset: Int): Flow<List<RecommendedEntryWithShow>>

    @Transaction
    @Query("SELECT * FROM recommended ORDER BY page ASC, id ASC")
    abstract fun entriesPagingSource(): PagingSource<Int, RecommendedEntryWithShow>

    @Query("DELETE FROM recommended WHERE page = :page")
    abstract override suspend fun deletePage(page: Int)

    @Query("DELETE FROM recommended")
    abstract override suspend fun deleteAll()

    @Query("SELECT MAX(page) from recommended")
    abstract override suspend fun getLastPage(): Int?
}