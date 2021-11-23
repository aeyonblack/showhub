package com.tanya.domain.interactors

import android.util.Log
import com.tanya.data.android.repository.images.ShowImagesStore
import com.tanya.data.android.repository.relatedshows.RelatedShowsStore
import com.tanya.data.android.repository.shows.ShowsStore
import com.tanya.data.extensions.fetch
import com.tanya.domain.Interactor
import com.tanya.domain.interactors.UpdateRelatedShows.Params
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateRelatedShows @Inject constructor(
    private val relatedShowsStore: RelatedShowsStore,
    private val showsStore: ShowsStore,
    private val showImagesStore: ShowImagesStore,
): Interactor<Params>() {

    override suspend fun doWork(params: Params) = withContext(Dispatchers.IO) {
        relatedShowsStore.fetch(params.showId, params.forceLoad).forEach {
            showsStore.fetch(it.otherShowId)
            try {
                showImagesStore.fetch(it.otherShowId)
            } catch (t: Throwable) {
                Log.d("updateRelatedShows",
                    "Error while fetching images for show, ${it.otherShowId}")
            }
        }
    }

    data class Params(val showId: Long, val forceLoad: Boolean)
}