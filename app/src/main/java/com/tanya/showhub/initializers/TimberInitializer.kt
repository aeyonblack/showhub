package com.tanya.showhub.initializers

import android.app.Application
import com.tanya.base.android.appinitializer.AppInitializer
import com.tanya.base.android.appinitializer.util.ShowhubLogger
import com.tanya.showhub.BuildConfig
import javax.inject.Inject

class TimberInitializer @Inject constructor(
    private val showhubLogger: ShowhubLogger
) : AppInitializer {
    override fun init(application: Application) {
        showhubLogger.setup(BuildConfig.DEBUG)
    }
}