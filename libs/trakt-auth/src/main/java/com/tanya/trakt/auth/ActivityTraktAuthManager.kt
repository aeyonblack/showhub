package com.tanya.trakt.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.Lazy
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import javax.inject.Inject

@DelicateCoroutinesApi
internal class ActivityTraktAuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val traktManager: TraktManager,
    private val requestProvider: Lazy<AuthorizationRequest>,
    private val clientAuth: Lazy<ClientAuthentication>,
) : TraktAuthManager {
    private val authService by lazy(LazyThreadSafetyMode.NONE) {
        AuthorizationService(context)
    }

    override fun buildLoginIntent(): Intent {
        return authService.getAuthorizationRequestIntent(requestProvider.get())
    }

    override fun onLoginResult(result: LoginTrakt.Result) {
        val (response, error) = result
        when {
            response != null -> {
                authService.performTokenRequest(
                    response.createTokenExchangeRequest(),
                    clientAuth.get()
                ) { tokenResponse, ex ->
                    val state = AuthState().apply {
                        update(tokenResponse, ex)
                    }
                    traktManager.onNewAuthState(state)
                }
            }
            error != null -> Log.d("activityTraktManager", error.message.toString(), )
        }
    }
}