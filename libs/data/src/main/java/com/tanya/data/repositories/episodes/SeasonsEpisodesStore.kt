package com.tanya.data.repositories.episodes

import com.tanya.data.DatabaseTransactionRunner
import com.tanya.data.daos.EpisodesDao
import com.tanya.data.daos.SeasonsDao
import com.tanya.data.entities.EpisodeEntity
import com.tanya.data.entities.SeasonEntity
import com.tanya.data.results.EpisodeWithSeason
import com.tanya.data.results.SeasonWithEpisodesAndWatches
import com.tanya.data.syncers.syncerForEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class SeasonsEpisodesStore @Inject constructor(
    private val transactionRunner: DatabaseTransactionRunner,
    private val seasonsDao: SeasonsDao,
    private val episodesDao: EpisodesDao,
) {
    private val seasonSyncer = syncerForEntity(
        seasonsDao,
        { it.traktId },
        { entity, id -> entity.copy(id = id ?: 0) },
    )

    private val episodeSyncer = syncerForEntity(
        episodesDao,
        { it.traktId },
        { entity, id -> entity.copy(id = id ?: 0) },
    )

    fun observeEpisode(episodeId: Long): Flow<EpisodeWithSeason> {
        return episodesDao.episodeWithIdObservable(episodeId)
    }

    fun observeShowSeasons(showId: Long): Flow<List<SeasonEntity>> {
        return seasonsDao.observeSeasonsForShowId(showId)
    }

    fun observeShowSeasonsWithEpisodes(showId: Long): Flow<List<SeasonWithEpisodesAndWatches>> {
        return seasonsDao.seasonsWithEpisodesForShowId(showId)
    }

    fun observePagedSeasonsWithEpisodes(showId: Long) =
        seasonsDao.pagedSeasonsWithEpisodesForShowId(showId)

    fun observeShowSeasonWithEpisodes(seasonId: Long): Flow<SeasonWithEpisodesAndWatches> {
        return seasonsDao.seasonWithEpisodes(seasonId)
    }

    fun observeShowNextEpisodeToWatch(showId: Long): Flow<EpisodeWithSeason?> {
        return episodesDao.observeLatestWatchedEpisodeForShowId(showId).flatMapLatest {
            episodesDao.observeNextAiredEpisodeForShowAfter(
                showId,
                it?.season?.number ?: 0,
                it?.episode?.number ?: 0
            )
        }
    }

    /**
     * Gets the ID for the season with the given trakt Id. If the trakt Id does not exist in the
     * database, it is inserted and the generated ID is returned.
     */
    suspend fun getEpisodeIdForTraktId(traktId: Int): Long? {
        return episodesDao.episodeIdWithTraktId(traktId)
    }

    suspend fun getSeason(id: Long) = seasonsDao.seasonWithId(id)

    suspend fun getSeasonWithTraktId(traktId: Int) = seasonsDao.seasonWithTraktId(traktId)

    suspend fun getEpisodesInSeason(seasonId: Long) = episodesDao.episodesWithSeasonId(seasonId)

    suspend fun getEpisode(id: Long) = episodesDao.episodeWithId(id)

    suspend fun getEpisodeWithTraktId(traktId: Int) = episodesDao.episodeWithTraktId(traktId)

    suspend fun updateSeasonFollowed(seasonId: Long, followed: Boolean) {
        seasonsDao.updateSeasonIgnoreFlag(seasonId, !followed)
    }

    suspend fun updatePreviousSeasonFollowed(
        seasonId: Long,
        followed: Boolean
    ) = transactionRunner {
        for (id in seasonsDao.showPreviousSeasonIds(seasonId)) {
            seasonsDao.updateSeasonIgnoreFlag(id, !followed)
        }
    }

    suspend fun save(episode: EpisodeEntity) = episodesDao.insertOrUpdate(episode)

    suspend fun save(showId: Long, data: Map<SeasonEntity, List<EpisodeEntity>>) = transactionRunner {
        seasonSyncer.sync(seasonsDao.seasonsForShowId(showId), data.keys)
        data.forEach { (season, episodes) ->
            val seasonId = seasonsDao.seasonWithTraktId(season.traktId!!)!!.id
            val updatedEpisodes = episodes.map { if (it.seasonId != seasonId) it.copy(seasonId = seasonId) else it }
            episodeSyncer.sync(episodesDao.episodesWithSeasonId(seasonId), updatedEpisodes)
        }
    }

    suspend fun deleteShowSeasonData(showId: Long) {
        // Due to foreign keys, this will also delete the episodes and watches
        seasonsDao.deleteSeasonsForShowId(showId)
    }
}