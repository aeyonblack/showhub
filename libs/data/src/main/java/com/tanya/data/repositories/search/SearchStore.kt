package com.tanya.data.repositories.search

import androidx.collection.LruCache
import javax.inject.Inject

class SearchStore @Inject constructor() {
    private val cache = LruCache<String, LongArray>(32)

    fun getResults(query: String) = cache[query]

    fun setResults(query: String, results: LongArray) {
        cache.put(query, results)
    }
}