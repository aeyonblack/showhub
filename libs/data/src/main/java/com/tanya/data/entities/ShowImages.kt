package com.tanya.data.entities

import com.tanya.data.entities.ImageType.*

/**
 * Util for getting the highest rated show images to display
 */
data class ShowImages(val images: List<ShowImagesEntity>) {

    val backdrop by lazy(LazyThreadSafetyMode.NONE) {
        findHighestRatedForType(BACKDROP)
    }

    val poster by lazy(LazyThreadSafetyMode.NONE) {
        findHighestRatedForType(POSTER)
    }

    private fun findHighestRatedForType(type: ImageType): ShowImagesEntity? {
        return images.filter { it.type == type }
            .maxByOrNull { it.rating + (if (it.isPrimary) 10f else 0f) }
    }

}