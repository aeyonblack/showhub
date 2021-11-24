package com.tanya.data.mappers

import com.tanya.data.entities.EpisodeEntity
import com.tanya.data.entities.SeasonEntity
import com.uwetrottmann.trakt5.entities.Season
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Singleton
class TraktSeasonToSeasonWithEpisodes @Inject constructor(
    private val seasonMapper: TraktSeasonToSeasonEntity,
    private val episodeMapper: TraktEpisodeToEpisodeEntity
): Mapper<Season, Pair<SeasonEntity, List<EpisodeEntity>>> {
    override suspend fun map(from: Season): Pair<SeasonEntity, List<EpisodeEntity>> =
        seasonMapper.map(from) to from.episodes.map { episodeMapper.map(it) }
}