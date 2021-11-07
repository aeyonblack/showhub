package com.tanya.data.repositories.shows

import com.tanya.data.entities.ShowEntity

interface ShowDataSource {
    suspend fun getShow(show: ShowEntity): Result<ShowEntity>
}