package com.tanya.trakt.auth

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.core.net.toUri
import com.tanya.base.di.ApplicationId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.openid.appauth.*
import javax.inject.Named
import javax.inject.Singleton

@DelicateCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object TraktAuthModule {

    @Singleton
    @Provides
    fun provideAuthConfig(): AuthorizationServiceConfiguration {
        return AuthorizationServiceConfiguration(
            Uri.parse("https://trakt.tv/oauth/authorize"),
            Uri.parse("https://trakt.tv/oauth/token")
        )
    }

    @Provides
    fun provideAuthState(traktManager: TraktManager) = runBlocking {
        traktManager.state.first()
    }

    @Provides
    fun provideAuthRequest(
        serviceConfig: AuthorizationServiceConfiguration,
        @Named("trakt-client-id") clientId: String,
        @Named("trakt-auth-redirect-uri") redirectUri: String
    ): AuthorizationRequest {
        return AuthorizationRequest.Builder(
            serviceConfig,
            clientId,
            ResponseTypeValues.CODE,
            redirectUri.toUri()
        ).apply {
            // Disable PKCE since Trakt does not support it
            setCodeVerifier(null)
        }.build()
    }

    @Singleton
    @Named("trakt-auth-redirect-uri")
    @Provides
    fun provideAuthRedirectUri(
        @ApplicationId applicationId: String
    ): String = "$applicationId://${TraktConstants.URI_AUTH_CALLBACK_PATH}"

    @Singleton
    @Provides
    fun provideClientAuth(
        @Named("trakt-client-secret") clientSecret: String
    ): ClientAuthentication {
        return ClientSecretBasic(clientSecret)
    }

    @Singleton
    @Provides
    @Named("auth")
    fun provideAuthSharedPrefs(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("trakt_auth", Context.MODE_PRIVATE)
    }
}