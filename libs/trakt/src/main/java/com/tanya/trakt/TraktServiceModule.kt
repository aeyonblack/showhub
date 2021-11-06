package com.tanya.trakt

import com.uwetrottmann.trakt5.TraktV2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TraktServiceModule {

    @Provides
    fun provideUserServices(traktV2: TraktV2) = traktV2.users()

    @Provides
    fun provideShowsServices(traktV2: TraktV2) = traktV2.shows()

    @Provides
    fun provideEpisodesServices(traktV2: TraktV2) = traktV2.episodes()

    @Provides
    fun provideSeasonsServices(traktV2: TraktV2) = traktV2.seasons()

    @Provides
    fun provideSyncServices(traktV2: TraktV2) = traktV2.sync()

    @Provides
    fun provideSearchServices(traktV2: TraktV2) = traktV2.search()

    @Provides
    fun provideRecommendationsServices(traktV2: TraktV2) = traktV2.recommendations()

}