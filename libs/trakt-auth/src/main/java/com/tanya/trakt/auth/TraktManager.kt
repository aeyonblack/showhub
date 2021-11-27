package com.tanya.trakt.auth

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TraktManager @Inject constructor(
    @Named("auth") private val authPrefs: SharedPreferences,

) {
}