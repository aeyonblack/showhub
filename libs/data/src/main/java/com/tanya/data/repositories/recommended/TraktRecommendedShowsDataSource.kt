package com.tanya.data.repositories.recommended

import com.tanya.base.data.entities.Result
import com.tanya.base.extensions.executeWithRetry
import com.tanya.base.extensions.toResult
import com.tanya.data.entities.ShowEntity
import com.tanya.data.mappers.TraktShowToShowEntity
import com.tanya.data.mappers.forLists
import com.uwetrottmann.trakt5.services.Recommendations
import javax.inject.Inject
import javax.inject.Provider

class TraktRecommendedShowsDataSource @Inject constructor(
    private val service: Provider<Recommendations>,
    private val mapper: TraktShowToShowEntity
) {
    suspend operator fun invoke(page: Int, pageSize: Int): Result<List<ShowEntity>> =
        service
            .get()
            .shows(page+1, pageSize, null)
            .executeWithRetry()
            .toResult(mapper.forLists())
}