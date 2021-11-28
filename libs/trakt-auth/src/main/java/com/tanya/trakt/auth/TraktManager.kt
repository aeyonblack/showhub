package com.tanya.trakt.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import com.tanya.base.actions.ShowTasks
import com.tanya.trakt.TraktAuthState
import com.uwetrottmann.trakt5.TraktV2
import dagger.Lazy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import net.openid.appauth.AuthState
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@DelicateCoroutinesApi
@Singleton
class TraktManager @Inject constructor(
    @Named("auth") private val authPrefs: SharedPreferences,
    private val showTasks: ShowTasks,
    private val traktClient: Lazy<TraktV2>
) {

    private val authState = MutableStateFlow(EmptyAuthState)

    private val _state = MutableStateFlow(TraktAuthState.LOGGED_OUT)

    private val state: StateFlow<TraktAuthState>
        get() = _state

    init {
        // Observer which updates local state
        GlobalScope.launch(Dispatchers.IO) {
            authState.collect { authState ->
                updateAuthState(authState)

                traktClient.get().apply {
                    accessToken(authState.accessToken)
                    refreshToken(authState.refreshToken)
                }
            }
        }

        // Read the auth state from prefs
        GlobalScope.launch(Dispatchers.Main) {
            val state = withContext(Dispatchers.IO) { readAuthState() }
            authState.value = state
        }
    }

    private fun updateAuthState(authState: AuthState) {
        if (authState.isAuthorized) {
            _state.value = TraktAuthState.LOGGED_IN
        } else {
            _state.value = TraktAuthState.LOGGED_OUT
        }
    }

    fun clearAuth() {
        authState.value = EmptyAuthState
        clearPersistedAuthState()
    }

    fun onNewAuthState(newState: AuthState) {
        GlobalScope.launch(Dispatchers.Main) {
            // Update our local state
            authState.value = newState
        }
        GlobalScope.launch(Dispatchers.IO) {
            // Persist auth state
            persistAuthState(newState)
        }
        // Now trigger a sync of all shows
        showTasks.syncFollowedShowsWhenIdle()
    }

    private fun readAuthState(): AuthState {
        val stateJson = authPrefs.getString(PreferenceAuthKey, null)
        return when {
            stateJson != null -> AuthState.jsonDeserialize(stateJson)
            else -> AuthState()
        }
    }

    private fun persistAuthState(state: AuthState) {
        authPrefs.edit(commit = true) {
            putString(PreferenceAuthKey, state.jsonSerializeString())
        }
    }

    private fun clearPersistedAuthState() {
        authPrefs.edit(commit = true) {
            remove(PreferenceAuthKey)
        }
    }

    companion object {
        private val EmptyAuthState = AuthState()
        private const val PreferenceAuthKey = "stateJson"
    }

}