package com.tanya.base.android.appinitializer

import android.app.Application

interface AppInitializer {
    fun init(application: Application)
}