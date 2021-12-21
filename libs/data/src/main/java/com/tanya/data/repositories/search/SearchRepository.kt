package com.tanya.data.repositories.search

import com.tanya.data.daos.ShowDao
import com.tanya.data.daos.ShowImagesDao
import com.tanya.data.results.ShowDetailed
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val searchStore: SearchStore,
    private val showImagesDao: ShowImagesDao,
    private val showDao: ShowDao,
    private val searchDataSource: TmdbSearchDataSource
) {

    suspend fun search(query: String): List<ShowDetailed> {
        if (query.isBlank()) return emptyList()

        val cacheValues = searchStore.getResults(query)
        if (cacheValues != null) return cacheValues.map {  }
    }

}