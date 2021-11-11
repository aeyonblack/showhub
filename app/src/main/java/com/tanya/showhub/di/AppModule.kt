package com.tanya.showhub.di

import android.app.Application
import com.tanya.base.di.ApplicationId
import com.tanya.showhub.BuildConfig
import com.tanya.tmdb.TmdbModule
import com.tanya.trakt.TraktModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module(
    includes = [
        TraktModule::class,
        TmdbModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object AppModule {

    @ApplicationId
    @Provides
    fun provideApplicationId(application: Application): String = application.packageName

    @Provides
    @Named("tmdb-api")
    fun provideTmdbApiKey(): String = BuildConfig.TMDB_API_KEY

    @Provides
    @Named("trakt-client-id")
    fun provideTraktClientId(): String = BuildConfig.TRAKT_CLIENT_ID

    @Provides
    @Named("trakt-client-secret")
    fun provideTraktClientSecret(): String = BuildConfig.TRAKT_CLIENT_SECRET

}