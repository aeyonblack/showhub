package com.tanya.data.repositories.trending

import com.tanya.data.mappers.TraktTrendingShowToShowEntity
import com.uwetrottmann.trakt5.services.Shows
import javax.inject.Inject
import javax.inject.Provider

class TraktTrendingShowsDataSource @Inject constructor(
    private val showService: Provider<Shows>,
    showMapper: TraktTrendingShowToShowEntity,
) {
}