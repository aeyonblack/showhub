package app.util

import com.tanya.base.data.entities.Success
import com.tanya.data.entities.ShowEntity
import com.tanya.data.repositories.shows.ShowDataSource

object FakeShowDataSource : ShowDataSource {
    override suspend fun getShow(show: ShowEntity) = Success(show)
}