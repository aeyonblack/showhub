package com.tanya.tmdb

private val IMAGE_SIZE_PATTERN = "w(\\d+)$".toRegex()

private fun selectSize(sizes: List<String>, imageWidth: Int): String {
    var previousSize: String? = null
    var previousWidth = 0

    for (i in sizes.indices) {
        val size = sizes[i]
        val sizeWidth = extractWidthAsIntFrom(size) ?: continue

        if (sizeWidth > imageWidth) {
            if (previousSize != null && imageWidth > (previousWidth + sizeWidth) / 2) {
                return size
            } else if (previousSize != null) {
                return previousSize
            }
        } else if (i == sizes.size - 1) {
            if (imageWidth < sizeWidth*2) {
                return size
            }
        }

        previousSize = size
        previousWidth = sizeWidth
    }
    return previousSize ?: sizes.last()
}

private fun extractWidthAsIntFrom(size: String) =
    IMAGE_SIZE_PATTERN.matchEntire(size)?.groups?.get(1)?.value?.toInt()

data class TmdbImageUrlProvider(
    private val baseImageUrl: String = TmdbImageSizes.baseImageUrl,
    private val backdropSizes: List<String> = TmdbImageSizes.backdropSizes,
    private val posterSizes: List<String> = TmdbImageSizes.posterSizes,
    private val logoSizes: List<String> = TmdbImageSizes.logoSizes
) {
    fun getBackdropUrl(path: String, imageWidth: Int) =
        "$baseImageUrl${selectSize(backdropSizes, imageWidth)}$path"

    fun getPosterUrl(path: String, imageWidth: Int) =
        "$baseImageUrl${selectSize(posterSizes, imageWidth)}$path"

    fun getLogoUrl(path: String, imageWidth: Int) =
        "$baseImageUrl${selectSize(logoSizes, imageWidth)}$path"
}