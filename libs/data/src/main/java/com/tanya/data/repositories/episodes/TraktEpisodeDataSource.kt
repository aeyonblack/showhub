package com.tanya.data.repositories.episodes

import com.tanya.base.data.entities.Result
import com.tanya.data.entities.EpisodeEntity
import com.tanya.data.mappers.ShowIdToTraktId
import com.uwetrottmann.trakt5.services.Episodes
import javax.inject.Inject
import javax.inject.Provider

class TraktEpisodeDataSource @Inject constructor(
    private val traktIdMapper: ShowIdToTraktId,
    private val service: Provider<Episodes>
): EpisodeDataSource {
    override suspend fun getEpisode(
        showId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<EpisodeEntity> {
        TODO("Not yet implemented")
    }
}