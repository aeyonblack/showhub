package com.tanya.data.repositories.shows

import com.tanya.data.entities.ShowEntity

fun mergeShows(
    local: ShowEntity = ShowEntity.EMPTY_SHOW,
    trakt: ShowEntity = ShowEntity.EMPTY_SHOW,
    tmdb: ShowEntity = ShowEntity.EMPTY_SHOW
) = local.copy(
    title = trakt.title ?: local.title,
    summary = trakt.summary ?: local.summary,
    homepage = trakt.homepage ?: local.homepage,
    certification = trakt.certification ?: local.certification,
    runtime = trakt.runtime ?: local.runtime,
    country = trakt.country ?: local.country,
    firstAired = trakt.firstAired ?: local.firstAired,
    _genres = trakt._genres ?: local._genres,
    status = trakt.status ?: local.status,
    airsDay = trakt.airsDay ?: local.airsDay,
    airsTimeZone = trakt.airsTimeZone ?: local.airsTimeZone,
    airsTime = trakt.airsTime ?: local.airsTime,

    traktId = trakt.traktId ?: local.traktId,
    traktRating = trakt.traktRating ?: local.traktRating,
    traktVotes = trakt.traktVotes ?: local.traktVotes,
    traktDataUpdate = trakt.traktDataUpdate ?: local.traktDataUpdate,

    tmdbId = tmdb.tmdbId ?: trakt.tmdbId ?: local.tmdbId,
    network = tmdb.network ?: trakt.network ?: local.network,
    networkLogoPath = tmdb.networkLogoPath ?: local.networkLogoPath
)