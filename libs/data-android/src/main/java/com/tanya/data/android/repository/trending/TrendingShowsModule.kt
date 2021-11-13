package com.tanya.data.android.repository.trending

import android.util.Log
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.tanya.base.data.entities.Success
import com.tanya.data.daos.ShowDao
import com.tanya.data.daos.TrendingDao
import com.tanya.data.entities.TrendingShowEntity
import com.tanya.data.repositories.trending.TraktTrendingShowsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

typealias TrendingShowsStore = Store<Int, List<TrendingShowEntity>>

@Module
@InstallIn(SingletonComponent::class)
object TrendingShowsModule {
    @Singleton
    @Provides
    fun provideTrendingShowsStore(
        traktTrendingShows: TraktTrendingShowsDataSource,
        trendingShowsDao: TrendingDao,
        showDao: ShowDao
    ): TrendingShowsStore = StoreBuilder.from(
        fetcher = Fetcher.of { page: Int ->
            traktTrendingShows(
                page = page,
                pageSize = 20
            ).also {
                if (page == 0 && it is Success) {
                    Log.d("trendingShowsModule", "TraktTrendingShowsDataSource Success")
                }
            }.getOrThrow()
        },
        sourceOfTruth = SourceOfTruth.Companion.of(
            reader = { page ->
                trendingShowsDao.entriesObservable(page).map {
                    when {
                        it.isEmpty() -> null
                        else -> it
                    }
                }
            },
            writer = { page, response ->
                trendingShowsDao.withTransaction {
                    val entries = response.map { (show, entry) ->
                        entry.copy(
                            showId = showDao.getIdOrSavePlaceholder(show),
                            page = page
                        )
                    }
                    if (page == 0) {
                        trendingShowsDao.deleteAll()
                        trendingShowsDao.insertAll(entries)
                    } else {
                        trendingShowsDao.updatePage(page, entries)
                    }
                }
            },
            delete = trendingShowsDao::deletePage,
            deleteAll = trendingShowsDao::deleteAll
        )
    ).build()
}

