package com.tanya.data.daos

import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Transaction
import com.tanya.data.PaginatedEntry
import com.tanya.data.results.EntryWithShow

abstract class PaginatedEntryDao<EC : PaginatedEntry, LI : EntryWithShow<EC>> : EntryDao<EC, LI>() {

    @Insert(onConflict = REPLACE)
    abstract override suspend fun insert(entity: EC): Long

    @Insert(onConflict = REPLACE)
    abstract override suspend fun insertAll(vararg entity: EC)

    @Insert(onConflict = REPLACE)
    abstract override suspend fun insertAll(entities: List<EC>)

    abstract suspend fun deletePage(page: Int)
    abstract suspend fun getLastPage(): Int?

    @Transaction
    open suspend fun updatePage(page: Int, entities: List<EC>) {
        deletePage(page)
        insertAll(entities)
    }
}