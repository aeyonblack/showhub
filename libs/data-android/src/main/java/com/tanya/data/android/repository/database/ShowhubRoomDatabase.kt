package com.tanya.data.android.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tanya.data.ShowhubDatabase
import com.tanya.data.entities.*
import com.tanya.data.views.FollowedShowsLastWatched
import com.tanya.data.views.FollowedShowsNextToWatch
import com.tanya.data.views.FollowedShowsWatchStats

@Database(
    entities = [
        ShowEntity::class,
        ShowFts::class,
        ShowImagesEntity::class,
        PopularShowEntity::class,
        TrendingShowEntity::class,
        RelatedShowEntity::class,
        FollowedShowEntity::class,
        EpisodeEntity::class,
        EpisodeWatchEntity::class,
        SeasonEntity::class,
        WatchedShowEntity::class,
    ],
    views = [
        FollowedShowsWatchStats::class,
        FollowedShowsLastWatched::class,
        FollowedShowsNextToWatch::class
    ],
    version = 1
)
@TypeConverters(ShowhubTypeConverters::class)
abstract class ShowhubRoomDatabase: RoomDatabase(), ShowhubDatabase