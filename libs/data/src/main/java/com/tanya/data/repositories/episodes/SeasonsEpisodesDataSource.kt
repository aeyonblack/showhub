package com.tanya.data.repositories.episodes

import com.tanya.base.data.entities.Result
import com.tanya.data.entities.EpisodeEntity
import com.tanya.data.entities.EpisodeWatchEntity
import com.tanya.data.entities.SeasonEntity
import org.threeten.bp.OffsetDateTime

interface SeasonsEpisodesDataSource {
    suspend fun getSeasonsEpisodes(showId:Long): Result<List<Pair<SeasonEntity, List<EpisodeEntity>>>>

    suspend fun getShowEpisodeWatches(
        showId: Long,
        since: OffsetDateTime? = null
    ): Result<List<Pair<EpisodeEntity, EpisodeWatchEntity>>>

    suspend fun getEpisodeWatches(
        episodeId:Long,
        since: OffsetDateTime? = null
    ): Result<List<EpisodeWatchEntity>>

    suspend fun getSeasonWatches(
        seasonId: Long,
        since: OffsetDateTime? = null
    ): Result<List<Pair<EpisodeEntity, EpisodeWatchEntity>>>

    suspend fun addEpisodeWatches(watches: List<EpisodeWatchEntity>): Result<Unit>

    suspend fun removeEpisodeWatches(watches: List<EpisodeWatchEntity>): Result<Unit>
}