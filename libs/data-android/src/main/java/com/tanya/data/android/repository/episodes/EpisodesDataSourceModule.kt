package com.tanya.data.android.repository.episodes

import com.tanya.base.di.Tmdb
import com.tanya.base.di.Trakt
import com.tanya.data.repositories.episodes.SeasonsEpisodesDataSource
import com.tanya.data.repositories.episodes.TmdbEpisodeDataSource
import com.tanya.data.repositories.episodes.TraktEpisodeDataSource
import com.tanya.data.repositories.episodes.TraktSeasonsEpisodesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class EpisodesDataSourceModule {

    @Binds
    @Tmdb
    abstract fun bindTmdbEpisodeDataSource(source: TmdbEpisodeDataSource): TmdbEpisodeDataSource

    @Binds
    @Trakt
    abstract fun bindTraktEpisodeDataSource(source: TraktEpisodeDataSource): TraktEpisodeDataSource

    @Binds
    abstract fun bindSeasonsEpisodesDataSource(source: TraktSeasonsEpisodesDataSource): SeasonsEpisodesDataSource

}