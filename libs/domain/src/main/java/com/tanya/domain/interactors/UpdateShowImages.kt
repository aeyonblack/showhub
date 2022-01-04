package com.tanya.domain.interactors

import com.tanya.base.util.AppCoroutineDispatchers
import com.tanya.data.android.repository.images.ShowImagesStore
import com.tanya.data.extensions.fetch
import com.tanya.domain.Interactor
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateShowImages @Inject constructor(
    private val showImagesStore: ShowImagesStore,
    private val dispatchers: AppCoroutineDispatchers
): Interactor<UpdateShowImages.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            showImagesStore.fetch(params.showId, params.forceLoad)
        }
    }

    data class Params(val showId: Long, val forceLoad: Boolean)
}