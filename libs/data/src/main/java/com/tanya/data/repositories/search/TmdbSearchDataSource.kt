package com.tanya.data.repositories.search

import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity
import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject

class TmdbSearchDataSource @Inject constructor(
    private val tmdb: Tmdb,
) : SearchDataSource {
    override suspend fun search(query: String): Result<List<Pair<ShowEntity, List<ShowImagesEntity>>>> {

    }
}