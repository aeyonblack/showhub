package com.tanya.data.repositories.images

import com.tanya.base.data.entities.Result
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity

interface ShowImagesDataSource {
    suspend fun getShowImages(show: ShowEntity) : Result<List<ShowImagesEntity>>
}