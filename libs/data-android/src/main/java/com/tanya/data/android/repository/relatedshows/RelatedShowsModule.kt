package com.tanya.data.android.repository.relatedshows

import android.util.Log
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.tanya.base.data.entities.Success
import com.tanya.data.daos.RelatedShowsDao
import com.tanya.data.daos.ShowDao
import com.tanya.data.entities.RelatedShowEntity
import com.tanya.data.repositories.relatedshows.TmdbRelatedShowsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

typealias RelatedShowsStore = Store<Long, List<RelatedShowEntity>>

@Module
@InstallIn(SingletonComponent::class)
internal object RelatedShowsModule {

    @Singleton
    @Provides
    fun provideRelatedShowsStore(
        tmdbRelatedShows: TmdbRelatedShowsDataSource,
        relatedShowsDao: RelatedShowsDao,
        showDao: ShowDao
    ): RelatedShowsStore = StoreBuilder.from(
        fetcher = Fetcher.of { showId: Long ->
            tmdbRelatedShows(showId)
                .also {
                    if (it is Success) {
                        Log.d("relatedShowsModule", "Successfully loaded related shows")
                    }
                }
                .getOrThrow()
        },
        sourceOfTruth = SourceOfTruth.Companion.of(
            reader = { showId ->
                relatedShowsDao.entriesObservable(showId).map { entries ->
                    when {
                        entries.isEmpty() -> null
                        else -> entries
                    }
                }
            },
            writer = { showId, response ->
                relatedShowsDao.withTransaction {
                    val entries = response.map { (show, entry) ->
                        entry.copy(
                            showId = showId,
                            otherShowId = showDao.getIdOrSavePlaceholder(show)
                        )
                    }
                    relatedShowsDao.deleteWithShowId(showId)
                    relatedShowsDao.insertOrUpdate(entries)
                }
            },
            delete = relatedShowsDao::deleteWithShowId,
            deleteAll = relatedShowsDao::deleteAll
        )
    ).build()
}