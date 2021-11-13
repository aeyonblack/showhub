package com.tanya.domain.interactors

import android.util.Log
import com.tanya.data.android.repository.images.ShowImagesStore
import com.tanya.data.android.repository.popular.PopularShowsStore
import com.tanya.data.android.repository.shows.ShowsStore
import com.tanya.data.daos.PopularDao
import com.tanya.data.extensions.fetch
import com.tanya.domain.Interactor
import com.tanya.domain.interactors.UpdatePopularShows.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdatePopularShows @Inject constructor(
    private val popularShowsStore: PopularShowsStore,
    private val popularDao: PopularDao,
    private val showsStore: ShowsStore,
    private val showImagesStore: ShowImagesStore,
): Interactor<Params>() {

    override suspend fun doWork(params: Params) {
        withContext(Dispatchers.IO) {
            val page = when {
                params.page >= 0 -> params.page
                params.page == Page.NEXT -> {
                    val lastPage = popularDao.getLastPage()
                    if (lastPage != null) lastPage + 1 else 0
                }
                else -> 0
            }
            Log.d("updatePopularShows", "popularShowStore.fetch() START")
            popularShowsStore.fetch(page, forceFresh = params.forceRefresh).forEach {
                Log.d("updatePopularShows", "showId ${it.showId}")
                showsStore.fetch(it.showId)
                try {
                    showImagesStore.fetch(it.showId)
                } catch (t: Throwable) {
                    Log.d("updatePopularShows",
                        "Error while fetching images for show ${it.showId}")
                }
            }
            Log.d("updatePopularShows", "popularShowStore.fetch() END")
        }
    }

    data class Params(val page: Int, val forceRefresh: Boolean = false)

    object Page {
        const val NEXT = -1
        const val REFRESH = -2
    }
}