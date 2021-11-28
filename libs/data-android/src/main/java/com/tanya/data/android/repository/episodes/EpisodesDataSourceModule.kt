package com.tanya.data.android.repository.episodes

import com.tanya.base.di.Tmdb
import com.tanya.base.di.Trakt
import com.tanya.data.repositories.episodes.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class EpisodesDataSourceModule {

    @Binds
    @Tmdb
    abstract fun bindTmdbEpisodeDataSource(source: TmdbEpisodeDataSource): EpisodeDataSource

    @Binds
    @Trakt
    abstract fun bindTraktEpisodeDataSource(source: TraktEpisodeDataSource): EpisodeDataSource

    @Binds
    abstract fun bindSeasonsEpisodesDataSource(source: TraktSeasonsEpisodesDataSource): SeasonsEpisodesDataSource

}
