package app.di

import app.util.FakeShowDataSource
import com.tanya.base.di.Tmdb
import com.tanya.base.di.Trakt
import com.tanya.data.repositories.episodes.EpisodeDataSource
import com.tanya.data.repositories.episodes.SeasonsEpisodesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk

@Module
@InstallIn(SingletonComponent::class)
object TestDataSourceModule {

    private val traktEpisodesDataSource: EpisodeDataSource = mockk()
    private val tmdbEpisodeDataSource: EpisodeDataSource = mockk()
    private val seasonsDataSource: SeasonsEpisodesDataSource = mockk()
    private val traktShowDataSource = FakeShowDataSource
    private val tmdbShowDataSource = FakeShowDataSource

    @Provides
    @Trakt
    fun provideTraktEpisodeDataSource() = traktEpisodesDataSource

    @Provides
    @Tmdb
    fun provideTmdbEpisodeDataSource() = tmdbEpisodeDataSource

    @Provides
    fun provideSeasonsDataSource() = seasonsDataSource

    @Provides
    @Trakt
    fun provideTraktShowDataSource() = traktShowDataSource

    @Provides
    @Tmdb
    fun provideTmdbShowDataSource() = tmdbShowDataSource

}