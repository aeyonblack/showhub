package com.tanya.data.mappers

import com.tanya.tmdb.TmdbImageUrlProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbImagesToSeasonImages @Inject constructor(
    private val tmdbImageUrlProvider: TmdbImageUrlProvider
) {
}