package com.tanya.data.android.repository.database

import com.tanya.data.ShowhubDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseDaoModule {

    @Provides
    fun provideShowDao(db: ShowhubDatabase) = db.showDao()

    @Provides
    fun provideShowImagesDao(db: ShowhubDatabase) = db.showImagesDao()

    @Provides
    fun providePopularDao(db: ShowhubDatabase) = db.popularDao()

    @Provides
    fun provideTrendingDao(db: ShowhubDatabase) = db.trendingDao()

    @Provides
    fun provideRelatedShowsDao(db: ShowhubDatabase) = db.relatedShowsDao()

}