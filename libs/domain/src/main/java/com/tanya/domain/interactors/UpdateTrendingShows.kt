package com.tanya.domain.interactors

import android.util.Log
import com.tanya.base.util.AppCoroutineDispatchers
import com.tanya.data.android.repository.images.ShowImagesStore
import com.tanya.data.android.repository.shows.ShowsStore
import com.tanya.data.android.repository.trending.TrendingShowsStore
import com.tanya.data.daos.TrendingDao
import com.tanya.data.extensions.fetch
import com.tanya.domain.Interactor
import com.tanya.domain.interactors.UpdateTrendingShows.Params
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateTrendingShows @Inject constructor(
    private val trendingShowsStore: TrendingShowsStore,
    private val trendingDao: TrendingDao,
    private val showsStore: ShowsStore,
    private val showImagesStore: ShowImagesStore,
    private val dispatchers: AppCoroutineDispatchers
): Interactor<Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val page = when {
                params.page >= 0 -> params.page
                params.page == Page.NEXT -> {
                    val lastPage = trendingDao.getLastPage()
                    if (lastPage != null) lastPage + 1 else 0
                }
                else -> 0
            }

            trendingShowsStore.fetch(page, params.forceRefresh).forEach {
                showsStore.fetch(it.showId)
                try {
                    showImagesStore.fetch(it.showId)
                } catch (t: Throwable) {
                    Log.d("updateTrendingShows", "Error while fetching images for show")
                }
            }
        }
    }

    data class Params(val page: Int, val forceRefresh: Boolean = false)

    object Page {
        const val NEXT = -1
        const val REFRESH = -2
    }
}