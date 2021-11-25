package com.tanya.data.android.repository.library

import com.tanya.data.repositories.followedshows.FollowedShowsDataSource
import com.tanya.data.repositories.followedshows.TraktFollowedShowsDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class FollowedShowsModule {
    @Binds
    abstract fun bind(source: TraktFollowedShowsDataSource): FollowedShowsDataSource
}