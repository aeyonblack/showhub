package com.tanya.data.entities

interface ShowhubEntity {
    val id: Long
}

interface TraktIdEntity {
    val traktId: Int?
}

interface TmdbIdEntity {
    val tmdbId: Int?
}

interface TmdbImageEntity {
    val path: String
    val type: ImageType
    val language: String?
    val rating: Float
    val isPrimary: Boolean
}

enum class ImageType(val storageKey: String) {
    BACKDROP("backdrop"),
    POSTER("poster"),
    LOGO("logo")
}

internal fun <T : TmdbImageEntity> Collection<T>.findHighestRatedPoster(): T? {
    if (size <= 1) return firstOrNull()
    return filter { it.type == ImageType.POSTER }
        .maxByOrNull { it.rating + (if (it.isPrimary) 10f else 0f) }
}

internal fun <T : TmdbImageEntity> Collection<T>.findHighestRatedBackdrop(): T? {
    if (size <= 1) return firstOrNull()
    return filter { it.type == ImageType.BACKDROP }
        .maxByOrNull { it.rating + (if (it.isPrimary) 10f else 0f) }
}