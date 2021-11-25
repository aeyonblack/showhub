package com.tanya.data.repositories.episodes

import com.tanya.base.data.entities.ErrorResult
import com.tanya.base.data.entities.Result
import com.tanya.base.extensions.executeWithRetry
import com.tanya.base.extensions.toResult
import com.tanya.data.entities.EpisodeEntity
import com.tanya.data.entities.EpisodeWatchEntity
import com.tanya.data.mappers.*
import com.uwetrottmann.trakt5.entities.UserSlug
import com.uwetrottmann.trakt5.enums.Extended
import com.uwetrottmann.trakt5.enums.HistoryType
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

    override suspend fun getSeasonsEpisodes(showId: Long) =
        seasonsService
            .get()
            .summary(showIdToTraktIdMapper.map(showId).toString(), Extended.FULLEPISODES)
            .executeWithRetry()
            .toResult(seasonMapper.forLists())

    override suspend fun getShowEpisodeWatches(
        showId: Long,
        since: OffsetDateTime?
    ): Result<List<Pair<EpisodeEntity, EpisodeWatchEntity>>> {
        val showTraktId = showIdToTraktIdMapper.map(showId)
            ?: return ErrorResult(IllegalArgumentException("Trakt Id for Id $showId does not exist"))

        return userService.get().history(
            UserSlug.ME,
            HistoryType.SHOWS,
            showTraktId,
            0,
            10000,
            Extended.NOSEASONS,
            since,
            null
        )
            .executeWithRetry()
            .toResult(pairMapperOf(episodeMapper, historyMapper))
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