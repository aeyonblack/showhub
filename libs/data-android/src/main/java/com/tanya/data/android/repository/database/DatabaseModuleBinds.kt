package com.tanya.data.android.repository.database

import com.tanya.data.DatabaseTransactionRunner
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModuleBinds {

    @Binds
    abstract fun bindDatabaseTransactionRunner(runner: RoomTransactionRunner): DatabaseTransactionRunner

}