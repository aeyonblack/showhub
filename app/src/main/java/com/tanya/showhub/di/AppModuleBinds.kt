package com.tanya.showhub.di

import com.tanya.base.android.appinitializer.AppInitializer
import com.tanya.base.android.appinitializer.util.ShowhubLogger
import com.tanya.base.util.Logger
import com.tanya.showhub.initializers.ThreetenBpInitializer
import com.tanya.showhub.initializers.TimberInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinds {

    @Singleton
    @Binds
    abstract fun provideLogger(logger: ShowhubLogger): Logger

    @Binds
    @IntoSet
    abstract fun bindTimberInitializer(initializer: TimberInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun bindThreeTenInitializer(initializer: ThreetenBpInitializer): AppInitializer

}