package com.tanya.tasks

import com.tanya.base.actions.ShowTasks
import com.tanya.base.android.appinitializer.AppInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TasksModuleBinds {

    @Binds
    @IntoSet
    abstract fun bindTasksInitializer(source: ShowTasksInitializer): AppInitializer

    @Binds
    @Singleton
    abstract fun bindShowhubActions(source: ShowTasksImpl): ShowTasks

}