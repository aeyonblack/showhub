package com.tanya.showhub.di

import android.content.Context
import com.tanya.showhub.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.LoggingEventListener
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor? =
        if (!BuildConfig.DEBUG) {
            null
        } else {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            }
        }

    @Singleton
    @Provides
    fun provideHttpEventListener(): LoggingEventListener.Factory? =
        if (!BuildConfig.DEBUG) {
            null
        } else {
            LoggingEventListener.Factory()
        }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor?,
        loggingEventListener: LoggingEventListener.Factory?,
        @ApplicationContext context: Context
    ) = OkHttpClient.Builder()
        .apply {
            if (httpLoggingInterceptor != null) {
                addInterceptor(httpLoggingInterceptor)
            }
            if (loggingEventListener != null) {
                eventListenerFactory(loggingEventListener)
            }
        }
        .cache(Cache(File(context.cacheDir, "api_cache"),  50L * 1024 * 1024))
        .connectionPool(ConnectionPool(10, 2, TimeUnit.MINUTES))
        .dispatcher(
            Dispatcher().apply {
                maxRequestsPerHost = 15
            }
        )
        .build()

}