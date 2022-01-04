package com.tanya.showhub.initializers

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tanya.base.android.appinitializer.AppInitializer
import com.tanya.base.util.AppCoroutineDispatchers
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.zone.ZoneRulesProvider
import javax.inject.Inject

class ThreetenBpInitializer @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers
) : AppInitializer {

    @OptIn(DelicateCoroutinesApi::class)
    override fun init(application: Application) {
        AndroidThreeTen.init(application)
        GlobalScope.launch(dispatchers.io) {
            ZoneRulesProvider.getAvailableZoneIds()
        }
    }

}