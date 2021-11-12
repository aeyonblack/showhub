package com.tanya.data.android.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tanya.data.ShowhubDatabase
import com.tanya.data.entities.PopularShowEntity
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity
import com.tanya.data.entities.TrendingShowEntity

@Database(
    entities = [
        ShowEntity::class,
        ShowImagesEntity::class,
        PopularShowEntity::class,
        TrendingShowEntity::class
    ],
    version = 1
)
@TypeConverters(TypeConverters::class)
abstract class ShowhubRoomDatabase: RoomDatabase(), ShowhubDatabase {
}