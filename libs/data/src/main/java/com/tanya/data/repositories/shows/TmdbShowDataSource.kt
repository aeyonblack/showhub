package com.tanya.data.repositories.shows

import com.tanya.base.data.entities.ErrorResult
import com.tanya.base.data.entities.Result
import com.tanya.base.extensions.executeWithRetry
import com.tanya.base.extensions.toResult
import com.tanya.data.entities.ShowEntity
import com.tanya.data.mappers.TmdbShowToShowEntity
import com.uwetrottmann.tmdb2.Tmdb
import java.lang.IllegalArgumentException
import javax.inject.Inject

class TmdbShowDataSource @Inject constructor(
    private val tmdb: Tmdb,
    private val mapper: TmdbShowToShowEntity
) : ShowDataSource {
    override suspend fun getShow(show: ShowEntity): Result<ShowEntity> {
        val tmdbId = show.tmdbId
        return if (tmdbId != null) {
            tmdb.tvService().tv(tmdbId, null)
                .executeWithRetry()
                .toResult(mapper::map)
        } else {
            ErrorResult(IllegalArgumentException("TmdbId for show does not exist [$show]"))
        }
    }
}