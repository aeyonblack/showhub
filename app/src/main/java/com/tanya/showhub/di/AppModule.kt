package com.tanya.showhub.di

import android.app.Application
import android.content.Context
import com.tanya.base.android.appinitializer.extensions.withLocale
import com.tanya.base.di.*
import com.tanya.showhub.BuildConfig
import com.tanya.tmdb.TmdbModule
import com.tanya.trakt.TraktModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import javax.inject.Named
import javax.inject.Singleton

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

    @Singleton
    @Provides
    @MediumDate
    fun provideMediumDateFormatter(
        @ApplicationContext context: Context
    ): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(context)
    }

    @Singleton
    @Provides
    @MediumDateTime
    fun provideDateTimeFormatter(
        @ApplicationContext context: Context
    ): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(context)
    }

    @Singleton
    @Provides
    @ShortDate
    fun provideShortDateFormatter(
        @ApplicationContext context: Context
    ): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(context)
    }

    @Singleton
    @Provides
    @ShortTime
    fun provideShortTimeFormatter(
        @ApplicationContext context: Context
    ): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(context)
    }

}