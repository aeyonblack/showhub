package com.tanya.data.android.repository.popular

import android.util.Log
import com.tanya.base.data.entities.Success
import com.tanya.data.daos.PopularDao
import com.tanya.data.daos.ShowDao
import com.tanya.data.entities.PopularShowEntity
import com.tanya.data.repositories.popular.TraktPopularShowsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import javax.inject.Singleton

typealias PopularShowsStore = Store<Int, List<PopularShowEntity>>

@Module
@InstallIn(SingletonComponent::class)
internal object PopularShowsModule {

    @Singleton
    @Provides
    fun providePopularShowsStore(
        traktPopularShows: TraktPopularShowsDataSource,
        popularShowsDao: PopularDao,
        showDao: ShowDao
    ): PopularShowsStore = StoreBuilder.from(
        fetcher = Fetcher.of { page: Int ->
            traktPopularShows(
                page = page,
                pageSize = 20
            ).also {
                if (page == 0 && it is Success) {
                    Log.d("popularShowsModule", "traktPopularShowsDataSource Success")
                }
            }.getOrThrow()
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { page ->
                popularShowsDao.entriesObservable(page).map {
                    when {
                        it.isEmpty() -> null
                        else -> it
                    }
                }
            },
            writer = { page, response ->
                popularShowsDao.withTransaction {
                    val entries = response.map { (show, entry) ->
                        entry.copy(
                            showId = showDao.getIdOrSavePlaceholder(show),
                            page = page
                        )
                    }
                    if (page == 0) {
                        popularShowsDao.deleteAll()
                        popularShowsDao.insertAll(entries)
                    } else {
                        popularShowsDao.updatePage(page, entries)
                    }
                }
            },
            delete = popularShowsDao::deletePage,
            deleteAll = popularShowsDao::deleteAll
        )
    ).build()
}