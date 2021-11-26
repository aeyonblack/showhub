package com.tanya.trakt

import com.uwetrottmann.trakt5.TraktV2
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [TraktServiceModule::class])
object TraktModule {

    @Singleton
    @Provides
    fun provideTrakt(
        client: OkHttpClient,
        @Named("trakt-client-id") clientId: String,
        @Named("trakt-client-secret") clientSecret: String
    ) = object : TraktV2(clientId, clientSecret, "TODO") {
        override fun okHttpClient() =
            client.newBuilder().apply {
                setOkHttpClientDefaults(this)
                connectTimeout(20, SECONDS)
                readTimeout(20, SECONDS)
                writeTimeout(20, SECONDS)
            }.build()
    }

    @Singleton
    @Provides
    fun provideTempTraktAuthState() = TraktAuthState.LOGGED_OUT

}