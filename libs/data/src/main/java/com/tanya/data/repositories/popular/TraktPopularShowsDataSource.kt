package com.tanya.data.repositories.popular

import com.tanya.base.data.entities.Result
import com.tanya.base.extensions.executeWithRetry
import com.tanya.base.extensions.toResult
import com.tanya.data.entities.PopularShowEntity
import com.tanya.data.entities.ShowEntity
import com.tanya.data.mappers.IndexedMapper
import com.tanya.data.mappers.TraktShowToShowEntity
import com.tanya.data.mappers.pairMapperOf
import com.uwetrottmann.trakt5.entities.Show
import com.uwetrottmann.trakt5.enums.Extended
import com.uwetrottmann.trakt5.services.Shows
import javax.inject.Inject
import javax.inject.Provider

class TraktPopularShowsDataSource @Inject constructor(
    private val showService: Provider<Shows>,
    showMapper: TraktShowToShowEntity
) {
    private val entityMapper = object : IndexedMapper<Show, PopularShowEntity> {
        override suspend fun map(index: Int, from: Show) = PopularShowEntity(
            showId = 0,
            pageOrder = index,
            page = 0
        )
    }

    private val responseMapper = pairMapperOf(showMapper, entityMapper)

    suspend operator fun invoke(
        page: Int,
        pageSize: Int
    ) : Result<List<Pair<ShowEntity, PopularShowEntity>>> = showService.get().popular(
        page + 1,
        pageSize,
        Extended.NOSEASONS
    ).executeWithRetry().toResult(responseMapper)
}