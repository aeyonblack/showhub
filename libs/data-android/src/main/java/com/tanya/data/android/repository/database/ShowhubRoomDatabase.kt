package com.tanya.data.android.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tanya.data.ShowhubDatabase
import com.tanya.data.entities.*

@Database(
    entities = [
        ShowEntity::class,
        ShowImagesEntity::class,
        PopularShowEntity::class,
        TrendingShowEntity::class,
        RelatedShowEntity::class
    ],
    version = 1
)
@TypeConverters(ShowhubTypeConverters::class)
abstract class ShowhubRoomDatabase: RoomDatabase(), ShowhubDatabase