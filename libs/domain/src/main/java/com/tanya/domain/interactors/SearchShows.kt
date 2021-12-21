package com.tanya.domain.interactors

import com.tanya.data.daos.ShowFtsDao
import com.tanya.data.repositories.search.SearchRepository
import com.tanya.data.results.ShowDetailed
import com.tanya.domain.SuspendingWorkInteractor
import com.tanya.domain.interactors.SearchShows.Params
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchShows @Inject constructor(
    private val searchRepository: SearchRepository,
    private val showFtsDao: ShowFtsDao,

) : SuspendingWorkInteractor<Params, List<ShowDetailed>>() {

    override suspend fun doWork(params: Params) =
        withContext(Dispatchers.IO) {
            val remoteResults = searchRepository.search(params.query)
            remoteResults.ifEmpty {
                when {
                    params.query.isNotBlank() -> showFtsDao.search("*${params.query}*")
                    else -> emptyList()
                }
            }
        }

    data class Params(val query: String)
}