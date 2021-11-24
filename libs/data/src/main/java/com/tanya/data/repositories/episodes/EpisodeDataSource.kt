package com.tanya.data.repositories.episodes

import com.tanya.base.data.entities.Result
import com.tanya.data.entities.EpisodeEntity

interface EpisodeDataSource {
    suspend fun getEpisode(
        showId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<EpisodeEntity>
}