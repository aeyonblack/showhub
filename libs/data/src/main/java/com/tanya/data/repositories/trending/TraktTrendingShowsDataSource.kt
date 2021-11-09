package com.tanya.data.repositories.trending

import com.tanya.base.data.entities.Result
import com.tanya.base.extensions.executeWithRetry
import com.tanya.base.extensions.toResult
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.TrendingShowEntity
import com.tanya.data.mappers.TraktTrendingShowToShowEntity
import com.tanya.data.mappers.TraktTrendingShowToTrendingShowEntity
import com.tanya.data.mappers.pairMapperOf
import com.uwetrottmann.trakt5.enums.Extended
import com.uwetrottmann.trakt5.services.Shows
import javax.inject.Inject
import javax.inject.Provider

class TraktTrendingShowsDataSource @Inject constructor(
    private val showService: Provider<Shows>,
    showMapper: TraktTrendingShowToShowEntity,
    entityMapper: TraktTrendingShowToTrendingShowEntity
) {
    private val responseMapper = pairMapperOf(showMapper, entityMapper)

    suspend operator fun invoke(
        page: Int,
        pageSize: Int
    ) : Result<List<Pair<ShowEntity, TrendingShowEntity>>> {
        return showService.get().trending(page + 1, pageSize, Extended.NOSEASONS)
            .executeWithRetry()
            .toResult(responseMapper)
    }
}