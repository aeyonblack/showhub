package com.tanya.domain.interactors

import com.tanya.base.util.AppCoroutineDispatchers
import com.tanya.data.android.repository.shows.ShowsStore
import com.tanya.data.extensions.fetch
import com.tanya.domain.Interactor
import com.tanya.domain.interactors.UpdateShowDetails.Params
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateShowDetails @Inject constructor(
    private val showsStore: ShowsStore,
    private val dispatchers: AppCoroutineDispatchers
): Interactor<Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            showsStore.fetch(params.showId, params.forceLoad)
        }
    }

    data class Params(val showId: Long, val forceLoad: Boolean)
}