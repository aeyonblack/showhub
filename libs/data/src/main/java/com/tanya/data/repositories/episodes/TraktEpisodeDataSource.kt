package com.tanya.data.repositories.episodes

import com.tanya.base.data.entities.ErrorResult
import com.tanya.base.data.entities.Result
import com.tanya.base.extensions.executeWithRetry
import com.tanya.base.extensions.toResult
import com.tanya.data.entities.EpisodeEntity
import com.tanya.data.mappers.ShowIdToTraktId
import com.tanya.data.mappers.TraktEpisodeToEpisodeEntity
import com.uwetrottmann.trakt5.enums.Extended
import com.uwetrottmann.trakt5.services.Episodes
import javax.inject.Inject
import javax.inject.Provider

class TraktEpisodeDataSource @Inject constructor(
    private val traktIdMapper: ShowIdToTraktId,
    private val service: Provider<Episodes>,
    private val mapper: TraktEpisodeToEpisodeEntity
): EpisodeDataSource {
    override suspend fun getEpisode(
        showId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<EpisodeEntity> {
        val traktId = traktIdMapper.map(showId)
            ?: return ErrorResult(IllegalArgumentException("No trakt id for show with id = $showId"))
        return service
            .get()
            .summary(traktId.toString(), seasonNumber, episodeNumber, Extended.FULL)
            .executeWithRetry()
            .toResult(mapper::map)
    }
}