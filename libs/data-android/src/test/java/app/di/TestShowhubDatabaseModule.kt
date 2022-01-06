package app.di

import com.tanya.base.util.Logger
import com.uwetrottmann.tmdb2.Tmdb
import com.uwetrottmann.trakt5.TraktV2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestShowhubDatabaseModule {

    @Provides
    fun provideTrakt() = TraktV2("fake_api_key_123")

    @Provides
    fun provideTmdb() = Tmdb("fake_api_key_123")

    @Singleton
    @Provides
    fun provideLogger(): Logger = mockk(relaxUnitFun = true)

}