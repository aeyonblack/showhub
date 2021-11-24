package com.tanya.data.daos

import androidx.room.Dao
import com.tanya.data.entities.FollowedShowEntity
import com.tanya.data.results.FollowedShowEntryWithShow

@Dao
abstract class FollowedShowsDao : EntryDao<FollowedShowEntity, FollowedShowEntryWithShow>() {
}