package com.tanya.showhub.di

import com.tanya.base.android.appinitializer.AppInitializer
import com.tanya.showhub.initializers.TimberInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinds {

    @Binds
    @IntoSet
    abstract fun bindTimberInitializer(logger: TimberInitializer): AppInitializer

}