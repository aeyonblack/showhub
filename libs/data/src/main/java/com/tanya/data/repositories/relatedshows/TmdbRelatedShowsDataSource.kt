package com.tanya.data.repositories.relatedshows

import com.tanya.base.data.entities.Result
import com.tanya.base.extensions.executeWithRetry
import com.tanya.base.extensions.toResult
import com.tanya.data.entities.RelatedShowEntity
import com.tanya.data.entities.ShowEntity
import com.tanya.data.mappers.*
import com.uwetrottmann.tmdb2.Tmdb
import com.uwetrottmann.tmdb2.entities.BaseTvShow
import javax.inject.Inject

class TmdbRelatedShowsDataSource @Inject constructor(
    private val tmdbIdMapper: ShowIdToTmdbIdMapper,
    private val tmdb: Tmdb,
    showMapper: TmdbBaseShowToShowEntity
) {

    private val entryMapper = object : IndexedMapper<BaseTvShow, RelatedShowEntity> {
        override suspend fun map(index: Int, from: BaseTvShow): RelatedShowEntity {
            return RelatedShowEntity(
                showId = 0,
                otherShowId = 0,
                orderIndex = index
            )
        }
    }

    private val resultMapper = unwrapTmdbShowResults(pairMapperOf(showMapper, entryMapper))

    suspend operator fun invoke(showId: Long): Result<List<Pair<ShowEntity, RelatedShowEntity>>> {
        return tmdb.tvService()
            .recommendations(tmdbIdMapper.map(showId), 1, null)
            .executeWithRetry()
            .toResult(resultMapper)
    }

}