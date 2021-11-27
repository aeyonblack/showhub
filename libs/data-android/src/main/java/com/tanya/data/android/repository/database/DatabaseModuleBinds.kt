package com.tanya.data.android.repository.database

import com.tanya.data.DatabaseTransactionRunner
import com.tanya.data.ShowhubDatabase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModuleBinds {

    @Binds
    abstract fun bindTiviDatabase(db: ShowhubRoomDatabase): ShowhubDatabase

    @Singleton
    @Binds
    abstract fun bindDatabaseTransactionRunner(runner: RoomTransactionRunner): DatabaseTransactionRunner

}
