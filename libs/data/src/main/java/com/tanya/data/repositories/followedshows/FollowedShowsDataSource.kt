package com.tanya.data.repositories.followedshows

import com.tanya.base.data.entities.Result
import com.tanya.data.entities.FollowedShowEntity
import com.tanya.data.entities.ShowEntity

interface FollowedShowsDataSource {
    suspend fun getListShows(listId: Int): Result<List<Pair<FollowedShowEntity, ShowEntity>>>
    suspend fun addShowIdsToList(listId: Int, shows: List<ShowEntity>): Result<Unit>
    suspend fun removeShowIdsFromList(listId: Int, shows: List<ShowEntity>): Result<Unit>
    suspend fun getFollowedListId(): Result<Int>
}