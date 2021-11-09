package com.tanya.data.repositories.shows

import com.tanya.base.data.entities.ErrorResult
import com.tanya.base.data.entities.Result
import com.tanya.base.data.entities.Success
import com.tanya.base.extensions.toResult
import com.tanya.data.entities.ShowEntity
import com.tanya.data.mappers.TraktShowToShowEntity
import com.uwetrottmann.trakt5.enums.Extended
import com.uwetrottmann.trakt5.enums.IdType
import com.uwetrottmann.trakt5.enums.Type
import com.uwetrottmann.trakt5.services.Search
import com.uwetrottmann.trakt5.services.Shows
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider

@Suppress("BlockingMethodInNonBlockingContext")
class TraktShowDataSource @Inject constructor(
    private val showService: Provider<Shows>,
    private val searchService: Provider<Search>,
    private val mapper: TraktShowToShowEntity
) : ShowDataSource {
    override suspend fun getShow(show: ShowEntity): Result<ShowEntity> {
        var traktId = show.traktId

        if (traktId == null && show.tmdbId != null) {
            val response = searchService.get().idLookup(
                IdType.TMDB,
                show.tmdbId.toString(),
                Type.SHOW,
                Extended.NOSEASONS,
                1,
                1
            ).execute().toResult { it.getOrNull(0)?.show?.ids?.trakt }
            if (response is Success) {
                traktId = response.get()
            } else if (response is ErrorResult) {
                return ErrorResult(response.throwable)
            }
        }

        if (traktId == null) {
            val response = searchService.get().textQueryShow(
                show.title, null, null, null, show.country,
                null, null, null, show.network, null,
                Extended.NOSEASONS, 1, 1
            ).execute().toResult { it[0].show?.ids?.trakt }
            if (response is Success) {
                traktId = response.get()
            } else if (response is ErrorResult) {
                return ErrorResult(response.throwable)
            }
        }

        return if (traktId != null) {
            showService.get().summary(traktId.toString(), Extended.FULL)
                .execute()
                .toResult(mapper::map)
        } else {
            ErrorResult(IllegalArgumentException("Trakt Id for show does not exist, [$show]"))
        }
    }
}