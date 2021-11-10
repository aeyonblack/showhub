package com.tanya.data.android.repository.images

import com.dropbox.android.external.store4.Store
import com.tanya.base.di.Tmdb
import com.tanya.data.entities.ShowImagesEntity
import com.tanya.data.repositories.images.ShowImagesDataSource
import com.tanya.data.repositories.images.TmdbShowImagesDataSource
import dagger.Binds

typealias ShowImagesStore = Store<Long, List<ShowImagesEntity>>

internal abstract class ShowDataSourceBinds {

    @Binds
    @Tmdb
    abstract fun bindShowImagesDataSource(source: TmdbShowImagesDataSource): ShowImagesDataSource
}
