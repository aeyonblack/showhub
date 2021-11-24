package com.tanya.data.results

import com.tanya.data.entities.EpisodeEntity

val List<EpisodeWithWatches>.numberAiredToWatch: Int
    get() = count { !it.isWatched && it.episode.isAired }

val List<EpisodeWithWatches>.numberWatched: Int
    get() = count { it.isWatched }

val List<EpisodeWithWatches>.numberToAir: Int
    get() = size - numberAired

val List<EpisodeWithWatches>.numberAired: Int
    get() = count { it.episode.isAired }

val List<EpisodeWithWatches>.nextToAir: EpisodeEntity?
    get() = firstOrNull {
        it.episode.let { ep -> !ep.isAired && ep.firstAired != null }
    }?.episode