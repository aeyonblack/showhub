package com.tanya.trakt.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
internal abstract class TraktAuthManagerBinds {

    @Binds
    abstract fun bindTraktAuthManager(bind: ActivityTraktAuthManager): TraktAuthManager

}