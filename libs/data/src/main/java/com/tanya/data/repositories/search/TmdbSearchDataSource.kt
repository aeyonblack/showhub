package com.tanya.data.repositories.search

import com.tanya.base.data.entities.ErrorResult
import com.tanya.base.extensions.executeWithRetry
import com.tanya.base.extensions.toResult
import com.tanya.data.mappers.TmdbShowResultsPageToShows
import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject

class TmdbSearchDataSource @Inject constructor(
    private val tmdb: Tmdb,
    private val mapper: TmdbShowResultsPageToShows
) : SearchDataSource {
    override suspend fun search(query: String) = try {
        tmdb
            .searchService()
            .tv(query, 1, null, null, false)
            .executeWithRetry()
            .toResult(mapper::map)
    } catch (t: Throwable) {
        ErrorResult(t)
    }
}