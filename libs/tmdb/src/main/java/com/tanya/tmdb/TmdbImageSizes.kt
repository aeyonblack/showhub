package com.tanya.tmdb

/**
 * Holds sizes for different image types as specified by tmdb
 */
object TmdbImageSizes {

    // base url for getting poster and other images
    const val baseImageUrl = "https://image.tmdb.org/t/p/"

    val backdropSizes = listOf(
        "w300",
        "w780",
        "w1280",
        "original"
    )

    val posterSizes = listOf(
        "w92",
        "w154",
        "w185",
        "w342",
        "w500",
        "w780",
        "original"
    )

    val logoSizes = listOf(
        "w45",
        "w92",
        "w154",
        "w185",
        "w300",
        "w500",
        "original"
    )

}