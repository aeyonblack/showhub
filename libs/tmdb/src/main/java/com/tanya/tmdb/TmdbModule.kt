package com.tanya.tmdb

import com.uwetrottmann.tmdb2.Tmdb
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Named
import javax.inject.Singleton

@Module
object TmdbModule {

    @Provides
    fun provideTmdbImageUrlProvider(tmdbManager: TmdbManager) =
        tmdbManager.getLatestImageProvider()

    @Singleton
    @Provides
    fun provideTmdb(
        client: OkHttpClient,
        @Named("tmdb-api") apiKey: String
    ) = object : Tmdb(apiKey) {
        override fun okHttpClient() =
            client.newBuilder().apply {
                setOkHttpClientDefaults(this)
                connectTimeout(20, SECONDS)
                readTimeout(20, SECONDS)
                writeTimeout(20, SECONDS)
            }.build()
    }

}