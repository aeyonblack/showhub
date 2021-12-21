package com.tanya.data.repositories.search

import com.tanya.base.data.entities.Result
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity

interface SearchDataSource {
    suspend fun search(query: String): Result<List<Pair<ShowEntity, List<ShowImagesEntity>>>>
}