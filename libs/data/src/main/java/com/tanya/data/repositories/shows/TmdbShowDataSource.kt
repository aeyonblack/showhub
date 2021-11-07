package com.tanya.data.repositories.shows

import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject

class TmdbShowDataSource @Inject constructor(
    private val tmdb: Tmdb,

) {
}