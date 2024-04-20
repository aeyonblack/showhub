package com.tanya.domain.observers

import com.tanya.base.util.AppCoroutineDispatchers
import com.tanya.data.android.repository.images.ShowImagesStore
import com.tanya.data.entities.ShowImages
import com.tanya.data.extensions.filterForResult
import com.tanya.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.StoreReadRequest
import javax.inject.Inject

class ObserveShowImages @Inject constructor(
    private val store: ShowImagesStore,
    private val dispatchers: AppCoroutineDispatchers
): SubjectInteractor<ObserveShowImages.Params, ShowImages>() {

    override fun createObservable(params: Params): Flow<ShowImages> =
        store.stream(StoreReadRequest.cached(params.showId, refresh = false))
            .filterForResult()
            .map { ShowImages(it.requireData()) }
            .flowOn(dispatchers.computation)

    data class Params(val showId: Long)
}