package com.tanya.data.android.repository.images

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.tanya.base.di.Tmdb
import com.tanya.data.daos.ShowDao
import com.tanya.data.daos.ShowImagesDao
import com.tanya.data.entities.ShowImagesEntity
import com.tanya.data.repositories.images.ShowImagesDataSource
import com.tanya.data.repositories.images.TmdbShowImagesDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.map
import java.lang.IllegalArgumentException
import javax.inject.Singleton

typealias ShowImagesStore = Store<Long, List<ShowImagesEntity>>

internal abstract class ShowDataSourceBinds {
    @Binds
    @Tmdb
    abstract fun bindShowImagesDataSource(source: TmdbShowImagesDataSource): ShowImagesDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object ShowImagesStoreModule {

    @Singleton
    @Provides
    fun provideShowImagesStore(
        showImagesDao: ShowImagesDao,
        showDao: ShowDao,
        @Tmdb showImagesDataSource: ShowImagesDataSource
    ): ShowImagesStore = StoreBuilder.from(
        fetcher = Fetcher.of { showId: Long ->
            val show = showDao.getShowWithId(showId)
                ?: throw IllegalArgumentException("Show with id $showId does not exist")
            val result = showImagesDataSource.getShowImages(show)

            result.getOrThrow().map {
                it.copy(showId)
            }
        },
        sourceOfTruth = SourceOfTruth.Companion.of(
            reader = { showId ->
                showImagesDao.getImagesForShowId(showId).map {
                    when {
                        it.isEmpty() -> null
                        else -> it
                    }
                }
            },
            writer = showImagesDao::saveImages,
            delete = showImagesDao::deleteForShowId,
            deleteAll = showImagesDao::deleteAll
        )
    ).build()
}