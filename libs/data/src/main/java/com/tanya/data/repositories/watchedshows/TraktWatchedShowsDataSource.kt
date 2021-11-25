package com.tanya.data.repositories.watchedshows

import com.tanya.base.data.entities.Result
import com.tanya.base.extensions.executeWithRetry
import com.tanya.base.extensions.toResult
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.WatchedShowEntity
import com.tanya.data.mappers.Mapper
import com.tanya.data.mappers.TraktBaseShowToShowEntity
import com.tanya.data.mappers.pairMapperOf
import com.uwetrottmann.trakt5.entities.BaseShow
import com.uwetrottmann.trakt5.enums.Extended
import com.uwetrottmann.trakt5.services.Sync
import javax.inject.Inject
import javax.inject.Provider

class TraktWatchedShowsDataSource @Inject constructor(
    private val syncService: Provider<Sync>,
    showMapper: TraktBaseShowToShowEntity
) {
    private val entryMapper = object : Mapper<BaseShow, WatchedShowEntity> {
        override suspend fun map(from: BaseShow): WatchedShowEntity {
            return WatchedShowEntity(showId = 0, lastWatched = from.last_watched_at!!)
        }
    }
    private val responseMapper = pairMapperOf(showMapper, entryMapper)

    suspend operator fun invoke(): Result<List<Pair<ShowEntity, WatchedShowEntity>>> {
        return syncService.get().watchedShows(Extended.NOSEASONS)
            .executeWithRetry()
            .toResult(responseMapper)
    }
}