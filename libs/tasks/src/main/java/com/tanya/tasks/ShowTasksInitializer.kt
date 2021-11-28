package com.tanya.tasks

import android.app.Application
import com.tanya.base.actions.ShowTasks
import com.tanya.base.android.appinitializer.AppInitializer
import dagger.Lazy
import javax.inject.Inject

class ShowTasksInitializer @Inject constructor(
    private val showTasks: Lazy<ShowTasks>
) : AppInitializer {
    override fun init(application: Application) {
        showTasks.get().setupNightSyncs()
    }
}