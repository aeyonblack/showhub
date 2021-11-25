package com.tanya.data.mappers

import com.tanya.data.entities.FollowedShowEntity
import com.uwetrottmann.trakt5.entities.ListEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraktListEntryToFollowedShowEntity @Inject constructor() : Mapper<ListEntry, FollowedShowEntity> {
    override suspend fun map(from: ListEntry) = FollowedShowEntity(
        showId = 0,
        followedAt = from.listed_at,
        traktId = from.id
    )
}