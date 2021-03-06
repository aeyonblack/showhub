package com.tanya.data.repositories.episodes

import com.tanya.base.data.entities.ErrorResult
import com.tanya.base.data.entities.Success
import com.tanya.base.di.Tmdb
import com.tanya.base.di.Trakt
import com.tanya.base.util.Logger
import com.tanya.data.entities.*
import com.tanya.data.extensions.instantInPast
import com.tanya.data.results.SeasonWithEpisodesAndWatches
import com.tanya.trakt.TraktAuthState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class SeasonsEpisodesRepository @Inject constructor(
    private val episodeWatchStore: EpisodeWatchStore,
    private val seasonsEpisodesStore: SeasonsEpisodesStore,
    private val traktSeasonsDataSource: SeasonsEpisodesDataSource,
    private val tmdbSeasonsDataSource: TmdbSeasonDataSource,
    @Trakt private val traktEpisodeDataSource: EpisodeDataSource,
    @Tmdb private val tmdbEpisodeDataSource: EpisodeDataSource,
    private val traktAuthState: Provider<TraktAuthState>,
    private val logger: Logger
) {
    fun observeSeasonsForShow(showId: Long): Flow<List<SeasonEntity>> {
        return seasonsEpisodesStore.observeShowSeasons(showId)
    }

    fun observeSeasonsWithEpisodesWatchedForShow(showId: Long): Flow<List<SeasonWithEpisodesAndWatches>> {
        return seasonsEpisodesStore.observeShowSeasonsWithEpisodes(showId)
    }

    fun observePagedSeasonsWithEpisodesWatchedForShow(showId: Long) =
        seasonsEpisodesStore.observePagedSeasonsWithEpisodes(showId)

    fun observeSeason(seasonId: Long) = seasonsEpisodesStore.observeShowSeasonWithEpisodes(seasonId)

    fun observeEpisode(episodeId: Long) = seasonsEpisodesStore.observeEpisode(episodeId)

    suspend fun getEpisode(episodeId: Long): EpisodeEntity? = seasonsEpisodesStore.getEpisode(episodeId)

    fun observeEpisodeWatches(episodeId: Long) = episodeWatchStore.observeEpisodeWatches(episodeId)

    fun observeNextEpisodeToWatch(showId: Long) = seasonsEpisodesStore.observeShowNextEpisodeToWatch(showId)

    suspend fun needShowSeasonsUpdate(
        showId: Long,
        expiry: Instant = instantInPast(days = 7)
    ): Boolean {
        return true //seasonsLastRequestStore.isRequestBefore(showId, expiry)
    }

    suspend fun removeShowSeasonData(showId: Long) {
        seasonsEpisodesStore.deleteShowSeasonData(showId)
    }

    suspend fun updateSeasonsEpisodes(showId: Long) {
        when (val response = traktSeasonsDataSource.getSeasonsEpisodes(showId)) {
            is Success -> mapSeasonsToEpisodes(showId, response.data)
            is ErrorResult -> throw response.throwable
        }
    }

    private suspend fun mapSeasonsToEpisodes(
        showId: Long,
        seasonsWithEpisodes: List<Pair<SeasonEntity, List<EpisodeEntity>>>
    ) {
        seasonsWithEpisodes
            .distinctBy { it.first.number }
            .associate { (season, episodes) ->
            val localSeason = seasonsEpisodesStore.getSeasonWithTraktId(season.traktId!!)
                ?: SeasonEntity(showId = showId)

            val mergedSeason = when (
                val tmdbResponse = tmdbSeasonsDataSource.getSeason(showId, season.number!!)
            ) {
                is Success -> mergeSeason(localSeason, season, tmdbResponse.data)
                else -> mergeSeason(localSeason, season, SeasonEntity.EMPTY)
            }

            val mergedEpisodes = episodes.distinctBy(EpisodeEntity::number).map {
                val localEpisode = seasonsEpisodesStore.getEpisodeWithTraktId(it.traktId!!)
                    ?: EpisodeEntity(seasonId = mergedSeason.id)
                mergeEpisode(localEpisode, it, EpisodeEntity.EMPTY)
            }

            mergedSeason to mergedEpisodes

        }.also {
                seasonsEpisodesStore.save(showId, it)
        }
    }

    suspend fun updateEpisode(episodeId: Long) = coroutineScope {
        val local = seasonsEpisodesStore.getEpisode(episodeId)!!
        val season = seasonsEpisodesStore.getSeason(local.seasonId)!!
        val traktResult = async {
            traktEpisodeDataSource.getEpisode(season.showId, season.number!!, local.number!!)
        }
        val tmdbResult = async {
            tmdbEpisodeDataSource.getEpisode(season.showId, season.number!!, local.number!!)
        }

        if (traktResult is ErrorResult<*> && tmdbResult is ErrorResult<*>) {
            throw traktResult.throwable
        }

        val trakt = traktResult.await().let {
            if (it is Success) it.data else EpisodeEntity.EMPTY
        }
        val tmdb = tmdbResult.await().let {
            if (it is Success) it.data else EpisodeEntity.EMPTY
        }

        seasonsEpisodesStore.save(mergeEpisode(local, trakt, tmdb))
    }

    suspend fun updateShowEpisodeWatches(
        showId: Long,
        refreshType: RefreshType = RefreshType.QUICK,
        forceRefresh: Boolean = false,
        lastUpdated: OffsetDateTime? = null
    ) {
        if (refreshType == RefreshType.QUICK) {
            if (lastUpdated != null /*&& episodeWatchLastLastRequestStore.hasBeenRequested(showId)*/) {
                if (forceRefresh || needShowEpisodeWatchesSync(showId, lastUpdated.toInstant())) {
                    updateShowEpisodeWatches(showId, lastUpdated.plusSeconds(1))
                }
            } else {
                if (forceRefresh || needShowEpisodeWatchesSync(showId)) {
                    updateShowEpisodeWatches(showId)
                }
            }
        } else if (refreshType == RefreshType.FULL) {
            if (forceRefresh || needShowEpisodeWatchesSync(showId)) {
                updateShowEpisodeWatches(showId)
            }
        }
    }

    private suspend fun updateShowEpisodeWatches(showId: Long, since: OffsetDateTime? = null) {
        if (traktAuthState.get() == TraktAuthState.LOGGED_IN) {
            fetchShowWatchesFromRemote(showId, since)
        }
    }

    suspend fun syncEpisodeWatchesForShow(showId: Long) {
        episodeWatchStore.getEntriesWithDeleteAction(showId).also {
            it.isNotEmpty() && processPendingDeletes(it)
        }

        episodeWatchStore.getEntriesWithAddAction(showId).also {
            it.isNotEmpty() && processPendingAdditions(it)
        }

        if (traktAuthState.get() == TraktAuthState.LOGGED_IN) {
            fetchShowWatchesFromRemote(showId)
        }
    }

    suspend fun needShowEpisodeWatchesSync(
        showId: Long,
        expiry: Instant = instantInPast(hours = 1)
    ): Boolean {
        return true /*episodeWatchLastLastRequestStore.isRequestBefore(showId, expiry)*/
    }

    suspend fun markSeasonWatched(seasonId: Long, onlyAired: Boolean, date: ActionDate) {
        val watchesToSave = seasonsEpisodesStore.getEpisodesInSeason(seasonId).mapNotNull { episode ->
            if (!onlyAired || episode.firstAired?.isBefore(OffsetDateTime.now()) == true) {
                if (!episodeWatchStore.hasEpisodeBeenWatched(episode.id)) {
                    val timestamp = when (date) {
                        ActionDate.NOW -> OffsetDateTime.now()
                        ActionDate.AIR_DATE -> episode.firstAired ?: OffsetDateTime.now()
                    }
                    return@mapNotNull EpisodeWatchEntity(
                        episodeId = episode.id,
                        watchedAt = timestamp,
                        pendingAction = PendingAction.UPLOAD
                    )
                }
            }
            null
        }

        if (watchesToSave.isNotEmpty()) {
            episodeWatchStore.save(watchesToSave)
        }

        val season = seasonsEpisodesStore.getSeason(seasonId)!!
        syncEpisodeWatchesForShow(season.showId)
    }

    suspend fun markSeasonUnwatched(seasonId: Long) {
        val season = seasonsEpisodesStore.getSeason(seasonId)!!

        val watches = ArrayList<EpisodeWatchEntity>()
        seasonsEpisodesStore.getEpisodesInSeason(seasonId).forEach { episode ->
            watches += episodeWatchStore.getWatchesForEpisode(episode.id)
        }
        if (watches.isNotEmpty()) {
            episodeWatchStore.updateEntriesWithAction(watches.map { it.id }, PendingAction.DELETE)
        }

        syncEpisodeWatchesForShow(season.showId)
    }

    suspend fun markSeasonFollowed(seasonId: Long) {
        seasonsEpisodesStore.updateSeasonFollowed(seasonId, true)
    }

    suspend fun markSeasonIgnored(seasonId: Long) {
        seasonsEpisodesStore.updateSeasonFollowed(seasonId, false)
    }

    suspend fun markPreviousSeasonsIgnored(seasonId: Long) {
        seasonsEpisodesStore.updatePreviousSeasonFollowed(seasonId, false)
    }

    suspend fun addEpisodeWatch(episodeId: Long, timestamp: OffsetDateTime) {
        val entry = EpisodeWatchEntity(
            episodeId = episodeId,
            watchedAt = timestamp,
            pendingAction = PendingAction.UPLOAD
        )
        episodeWatchStore.save(entry)

        syncEpisodeWatches(episodeId)
    }

    suspend fun removeEpisodeWatch(episodeWatchId: Long) {
        val episodeWatch = episodeWatchStore.getEpisodeWatch(episodeWatchId)
        if (episodeWatch != null && episodeWatch.pendingAction != PendingAction.DELETE) {
            episodeWatchStore.save(episodeWatch.copy(pendingAction = PendingAction.DELETE))
            syncEpisodeWatches(episodeWatch.episodeId)
        }
    }

    suspend fun removeAllEpisodeWatches(episodeId: Long) {
        val watchesForEpisode = episodeWatchStore.getWatchesForEpisode(episodeId)
        if (watchesForEpisode.isNotEmpty()) {
            // First mark them as pending deletion
            episodeWatchStore.updateEntriesWithAction(
                watchesForEpisode.map { it.id },
                PendingAction.DELETE
            )
            syncEpisodeWatches(episodeId)
        }
    }

    private suspend fun syncEpisodeWatches(episodeId: Long) {
        val watches = episodeWatchStore.getWatchesForEpisode(episodeId)
        var needUpdate = false

        val toDelete = watches.filter { it.pendingAction == PendingAction.DELETE }
        if (toDelete.isNotEmpty() && processPendingDeletes(toDelete)) {
            needUpdate = true
        }

        val toAdd = watches.filter { it.pendingAction == PendingAction.UPLOAD }
        if (toAdd.isNotEmpty() && processPendingAdditions(toAdd)) {
            needUpdate = true
        }

        if (needUpdate && traktAuthState.get() == TraktAuthState.LOGGED_IN) {
            fetchEpisodeWatchesFromRemote(episodeId)
        }
    }

    private suspend fun fetchShowWatchesFromRemote(showId: Long, since: OffsetDateTime? = null) {
        val response = traktSeasonsDataSource.getShowEpisodeWatches(showId, since).getOrThrow()

        val watches = response.mapNotNull { (episode, watchEntry) ->
            val epId = seasonsEpisodesStore.getEpisodeIdForTraktId(episode.traktId!!)
                ?: return@mapNotNull null
            watchEntry.copy(episodeId = epId)
        }
        if (since != null) {
            if (watches.isNotEmpty()) {
                episodeWatchStore.addNewShowWatchEntries(showId, watches)
            }
        } else {
            episodeWatchStore.syncShowWatchEntries(showId, watches)
        }
        //episodeWatchLastLastRequestStore.updateLastRequest(showId)
    }

    private suspend fun fetchEpisodeWatchesFromRemote(episodeId: Long) {
        val response = traktSeasonsDataSource.getEpisodeWatches(episodeId, null).getOrThrow()
        val watches = response.map { it.copy(episodeId = episodeId) }
        episodeWatchStore.syncEpisodeWatchEntries(episodeId, watches)
    }

    private suspend fun processPendingDeletes(entries: List<EpisodeWatchEntity>): Boolean {
        if (traktAuthState.get() == TraktAuthState.LOGGED_IN) {
            val localOnlyDeletes = entries.filter { it.traktId == null }
            if (localOnlyDeletes.isNotEmpty()) {
                episodeWatchStore.deleteEntriesWithIds(localOnlyDeletes.map(EpisodeWatchEntity::id))
            }

            if (entries.size > localOnlyDeletes.size) {
                val toRemove = entries.filter { it.traktId != null }
                when (val response = traktSeasonsDataSource.removeEpisodeWatches(toRemove)) {
                    is Success -> {
                        episodeWatchStore.deleteEntriesWithIds(entries.map(EpisodeWatchEntity::id))
                        return true
                    }
                    is ErrorResult -> throw response.throwable
                }
            }
        } else {
            episodeWatchStore.deleteEntriesWithIds(entries.map { it.id })
        }
        return false
    }

    private suspend fun processPendingAdditions(entries: List<EpisodeWatchEntity>): Boolean {
        if (traktAuthState.get() == TraktAuthState.LOGGED_IN) {
            when (val response = traktSeasonsDataSource.addEpisodeWatches(entries)) {
                is Success -> {
                    episodeWatchStore.updateEntriesWithAction(entries.map { it.id }, PendingAction.NOTHING)
                    return true
                }
                is ErrorResult -> throw response.throwable
            }
        } else {
            episodeWatchStore.updateEntriesWithAction(entries.map { it.id }, PendingAction.NOTHING)
        }
        return false
    }

    private fun mergeSeason(local: SeasonEntity, trakt: SeasonEntity, tmdb: SeasonEntity) = local.copy(
        title = trakt.title ?: local.title,
        summary = trakt.summary ?: local.summary,
        number = trakt.number ?: local.number,

        network = trakt.network ?: tmdb.network ?: local.network,
        episodeCount = trakt.episodeCount ?: tmdb.episodeCount ?: local.episodeCount,
        episodesAired = trakt.episodesAired ?: tmdb.episodesAired ?: local.episodesAired,

        // Trakt specific stuff
        traktId = trakt.traktId ?: local.traktId,
        traktRating = trakt.traktRating ?: local.traktRating,
        traktRatingVotes = trakt.traktRatingVotes ?: local.traktRatingVotes,

        // TMDb specific stuff
        tmdbId = tmdb.tmdbId ?: trakt.tmdbId ?: local.tmdbId,
        tmdbPosterPath = tmdb.tmdbPosterPath ?: local.tmdbPosterPath,
        tmdbBackdropPath = tmdb.tmdbBackdropPath ?: local.tmdbBackdropPath
    )

    private fun mergeEpisode(local: EpisodeEntity, trakt: EpisodeEntity, tmdb: EpisodeEntity) = local.copy(
        title = trakt.title ?: tmdb.title ?: local.title,
        summary = trakt.summary ?: tmdb.summary ?: local.summary,
        number = trakt.number ?: tmdb.number ?: local.number,
        firstAired = trakt.firstAired ?: tmdb.firstAired ?: local.firstAired,

        // Trakt specific stuff
        traktId = trakt.traktId ?: local.traktId,
        traktRating = trakt.traktRating ?: local.traktRating,
        traktRatingVotes = trakt.traktRatingVotes ?: local.traktRatingVotes,

        // TMDb specific stuff
        tmdbId = tmdb.tmdbId ?: trakt.tmdbId ?: local.tmdbId,
        tmdbBackdropPath = tmdb.tmdbBackdropPath ?: local.tmdbBackdropPath
    )
}