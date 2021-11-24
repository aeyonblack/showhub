package com.tanya.data.repositories.episodes

import com.tanya.base.data.entities.Result
import com.tanya.data.entities.EpisodeEntity
import javax.inject.Inject

class TraktEpisodeDataSource @Inject constructor(
): EpisodeDataSource {
    override suspend fun getEpisode(
        showId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<EpisodeEntity> {
        TODO("Not yet implemented")
    }
}