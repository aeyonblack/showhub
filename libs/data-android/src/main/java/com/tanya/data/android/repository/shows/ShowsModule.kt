package com.tanya.data.android.repository.shows

import android.util.Log
import com.tanya.base.data.entities.Success
import com.tanya.data.daos.ShowDao
import com.tanya.data.entities.ShowEntity
import com.tanya.data.repositories.shows.TraktShowDataSource
import com.tanya.data.repositories.shows.mergeShows
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

typealias ShowsStore = Store<Long, ShowEntity>

@Module
@InstallIn(SingletonComponent::class)
object ShowStoreModule {

    @Singleton
    @Provides
    fun provideShowStore(
        showDao: ShowDao,
        traktShowDataSource: TraktShowDataSource
    ): ShowsStore = StoreBuilder.from(
        fetcher = Fetcher.of { id: Long ->
            traktShowDataSource.getShow(showDao.getShowWithIdOrThrow(id))
                .also {
                    if (it is Success) {
                        Log.d("showsModule", "traktShowDataSource Successful")
                    }
                }.getOrThrow()
        },
        sourceOfTruth = SourceOfTruth.Companion.of(
            reader = { showId ->
                showDao.getShowWithIdFlow(showId).map {
                    it
                }
            },
            writer = { id, response ->
                showDao.withTransaction {
                    showDao.insertOrUpdate(
                        mergeShows(
                            local = showDao.getShowWithIdOrThrow(id),
                            trakt = response
                        )
                    )
                }
            },
            delete = showDao::delete,
            deleteAll = showDao::deleteAll
        )
    ).build()
}

