package com.tanya.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.tanya.data.results.ShowDetailed

@Dao
abstract class ShowFtsDao {

    @Transaction
    @Query(
        value =
        """
            SELECT s.* FROM shows as s
            INNER JOIN shows_fts AS fts ON s.id = fts.docid
            WHERE fts.title MATCH :filter
        """
    )
    abstract suspend fun search(filter: String): List<ShowDetailed>
}