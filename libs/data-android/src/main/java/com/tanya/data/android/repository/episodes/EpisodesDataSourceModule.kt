package com.tanya.data.android.repository.episodes

import com.tanya.data.repositories.episodes.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class EpisodesDataSourceModule {

    @Binds
    abstract fun bindTmdbEpisodeDataSource(source: TmdbEpisodeDataSource): EpisodeDataSource

    @Binds
    abstract fun bindTraktEpisodeDataSource(source: TraktEpisodeDataSource): EpisodeDataSource

    @Binds
    abstract fun bindSeasonsEpisodesDataSource(source: TraktSeasonsEpisodesDataSource): SeasonsEpisodesDataSource

}