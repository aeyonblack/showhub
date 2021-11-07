package com.tanya.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.tanya.data.entities.ShowImagesEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ShowImagesDao : EntityDao<ShowImagesEntity>(){

    @Query("DELETE FROM show_images WHERE show_id = :showId")
    abstract suspend fun deleteForShowId(showId: Long)

    @Query("SELECT COUNT(*) FROM show_images WHERE show_id = :showId")
    abstract suspend fun imageCountForShowId(showId: Long): Int

    @Query("SELECT * FROM show_images WHERE show_id = show_id")
    abstract fun getImagesForShowId(showId: Long): Flow<List<ShowImagesEntity>>

    @Query("DELETE FROM show_images")
    abstract suspend fun deleteAll()

    @Transaction
    open suspend fun saveImages(showId: Long, images: List<ShowImagesEntity>) {
        deleteForShowId(showId)
        insertOrUpdate(images)
    }

    open suspend fun saveImagesIfEmpty(showId: Long, images: List<ShowImagesEntity>) {
        if (imageCountForShowId(showId) <= 0) {
            insertAll(images)
        }
    }

}