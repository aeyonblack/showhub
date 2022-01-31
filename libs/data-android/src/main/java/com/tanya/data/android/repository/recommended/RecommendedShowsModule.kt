package com.tanya.data.android.repository.recommended

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.tanya.base.android.appinitializer.util.ShowhubLogger
import com.tanya.data.daos.RecommendedDao
import com.tanya.data.daos.ShowDao
import com.tanya.data.entities.RecommendedShowEntity
import com.tanya.data.repositories.recommended.TraktRecommendedShowsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

typealias RecommendedShowStore = Store<Int, List<RecommendedShowEntity>>

@Module
@InstallIn(SingletonComponent::class)
internal object RecommendedShowsModule {

    @Singleton
    @Provides
    fun provideRecommendedShowStore(
        recommendedShows: TraktRecommendedShowsDataSource,
        recommendedDao: RecommendedDao,
        showDao: ShowDao,
        logger: ShowhubLogger
    ) : RecommendedShowStore = StoreBuilder.from(
        fetcher = Fetcher.of { page: Int ->
            recommendedShows(page, 20)
                .also {
                    logger.d("Successfully loaded recommended shows")
                }
                .getOrThrow()
        },
        sourceOfTruth = SourceOfTruth.Companion.of(
            reader = { page ->
                recommendedDao.entriesForPage(page).map { entries ->
                    when {
                        entries.isEmpty() -> null
                        else -> entries
                    }
                }
            },
            writer = { page, response ->
                recommendedDao.withTransaction {
                    val entries = response.map { show ->
                        val showId = showDao.getIdOrSavePlaceholder(show)
                        RecommendedShowEntity(showId = showId, page = page)
                    }
                    if (page == 0) {
                        recommendedDao.deleteAll()
                        recommendedDao.insertAll(entries)
                    } else {
                        recommendedDao.updatePage(page, entries)
                    }
                }
            },
            delete = recommendedDao::deletePage,
            deleteAll = recommendedDao::deleteAll
        )
    ).build()
}