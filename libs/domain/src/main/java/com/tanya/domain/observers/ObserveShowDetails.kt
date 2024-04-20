package com.tanya.domain.observers

import com.tanya.base.util.AppCoroutineDispatchers
import com.tanya.data.android.repository.shows.ShowsStore
import com.tanya.data.entities.ShowEntity
import com.tanya.data.extensions.filterForResult
import com.tanya.domain.SubjectInteractor
import com.tanya.domain.observers.ObserveShowDetails.Params
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.StoreReadRequest
import javax.inject.Inject

class ObserveShowDetails @Inject constructor(
    private val showsStore: ShowsStore,
    private val dispatchers: AppCoroutineDispatchers
) : SubjectInteractor<Params, ShowEntity>() {

    override fun createObservable(params: Params): Flow<ShowEntity> {
        return showsStore.stream(StoreReadRequest.cached(params.showId, refresh = false))
            .filterForResult()
            .map { it.requireData() }
            .flowOn(dispatchers.computation)
    }

    data class Params(val showId: Long)
}