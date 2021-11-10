package com.tanya.domain.observers

import com.dropbox.android.external.store4.StoreRequest
import com.tanya.data.android.repository.images.ShowImagesStore
import com.tanya.data.entities.ShowImages
import com.tanya.data.extensions.filterForResult
import com.tanya.domain.SubjectInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveShowImages @Inject constructor(
    private val store: ShowImagesStore
): SubjectInteractor<ObserveShowImages.Params, ShowImages>() {

    override fun createObservable(params: Params): Flow<ShowImages> =
        store.stream(StoreRequest.cached(params.showId, refresh = false))
            .filterForResult()
            .map { ShowImages(it.requireData()) }
            .flowOn(Dispatchers.Default)

    data class Params(val showId: Long)
}