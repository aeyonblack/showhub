package com.tanya.data.repositories.episodes

import com.tanya.base.data.entities.Result
import com.tanya.data.entities.EpisodeEntity
import com.tanya.data.entities.EpisodeWatchEntity
import com.tanya.data.entities.SeasonEntity
import com.tanya.data.mappers.*
import com.uwetrottmann.trakt5.services.Seasons
import com.uwetrottmann.trakt5.services.Users
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Provider

class TraktSeasonsEpisodesDataSource @Inject constructor(
    private val showIdToTraktIdMapper: ShowIdToTraktIdMapper,
    private val seasonIdToTraktIdMapper: SeasonIdToTraktIdMapper,
    private val episodeIdToTraktIdMapper: EpisodeIdToTraktIdMapper,
    private val seasonMapper: TraktSeasonToSeasonWithEpisodes,
    private val episodeMapper: TraktHistoryEntryToEpisodeEntity,
    private val historyMapper: TraktHistoryItemToEpisodeWatchEntity,
    private val seasonsService: Provider<Seasons>,
    private val userService: Provider<Users>,
): SeasonsEpisodesDataSource {
    override suspend fun getSeasonsEpisodes(showId: Long): Result<List<Pair<SeasonEntity, List<EpisodeEntity>>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getShowEpisodeWatches(
        showId: Long,
        since: OffsetDateTime?
    ): Result<List<Pair<EpisodeEntity, EpisodeWatchEntity>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEpisodeWatches(
        episodeId: Long,
        since: OffsetDateTime?
    ): Result<List<EpisodeWatchEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSeasonWatches(
        seasonId: Long,
        since: OffsetDateTime?
    ): Result<List<Pair<EpisodeEntity, EpisodeWatchEntity>>> {
        TODO("Not yet implemented")
    }

    override suspend fun addEpisodeWatches(watches: List<EpisodeWatchEntity>): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun removeEpisodeWatches(watches: List<EpisodeWatchEntity>): Result<Unit> {
        TODO("Not yet implemented")
    }
}