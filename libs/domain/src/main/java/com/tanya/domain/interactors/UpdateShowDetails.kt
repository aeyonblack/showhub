package com.tanya.domain.interactors

import com.tanya.data.android.repository.shows.ShowsStore
import com.tanya.data.extensions.fetch
import com.tanya.domain.Interactor
import com.tanya.domain.interactors.UpdateShowDetails.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateShowDetails @Inject constructor(
    private val showsStore: ShowsStore
): Interactor<Params>() {

    override suspend fun doWork(params: Params) {
        withContext(Dispatchers.IO) {
            showsStore.fetch(params.showId, params.forceLoad)
        }
    }

    data class Params(val showId: Long, val forceLoad: Boolean)
}