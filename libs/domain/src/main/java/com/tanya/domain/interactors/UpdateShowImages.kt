package com.tanya.domain.interactors

import com.tanya.data.android.repository.images.ShowImagesStore
import com.tanya.data.extensions.fetch
import com.tanya.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateShowImages @Inject constructor(
    private val showImagesStore: ShowImagesStore
): Interactor<UpdateShowImages.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(Dispatchers.IO) {
            showImagesStore.fetch(params.showId, params.forceLoad)
        }
    }

    data class Params(val showId: Long, val forceLoad: Boolean)
}