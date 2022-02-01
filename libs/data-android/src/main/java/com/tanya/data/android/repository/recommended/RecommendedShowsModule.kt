package com.tanya.data.android.repository.recommended

import com.dropbox.android.external.store4.Store
import com.tanya.data.entities.RecommendedShowEntity
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

typealias RecommendedShowStore = Store<Int, List<RecommendedShowEntity>>

@Module
@InstallIn(SingletonComponent::class)
internal object RecommendedShowsModule {
    /*TODO*/
    /*@Singleton
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
    ).build()*/
}