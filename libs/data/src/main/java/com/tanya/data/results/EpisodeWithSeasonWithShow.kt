package com.tanya.data.results

import com.tanya.data.entities.*

data class EpisodeWithSeasonWithShow(
    val episode: EpisodeEntity,
    val season: SeasonEntity,
    val show: ShowEntity,
    private val images: List<ShowImagesEntity>
) {
    val backdrop by lazy(LazyThreadSafetyMode.NONE) {
        images.findHighestRatedBackdrop()
    }

    val poster by lazy(LazyThreadSafetyMode.NONE) {
        images.findHighestRatedPoster()
    }
}