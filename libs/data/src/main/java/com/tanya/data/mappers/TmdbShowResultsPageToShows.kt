package com.tanya.data.mappers

import com.tanya.data.entities.ImageType
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity
import com.tanya.tmdb.TmdbImageUrlProvider
import com.uwetrottmann.tmdb2.entities.TvShowResultsPage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbShowResultsPageToShows @Inject constructor(
    private val mapper: TmdbBaseShowToShowEntity,
    private val tmdbImageUrlProvider: TmdbImageUrlProvider
) : Mapper<TvShowResultsPage, List<Pair<ShowEntity, List<ShowImagesEntity>>>> {
    override suspend fun map(from: TvShowResultsPage): List<Pair<ShowEntity, List<ShowImagesEntity>>> =
        from.results.map {
            val show = mapper.map(it)
            val images = ArrayList<ShowImagesEntity>()

            if (it.poster_path != null) {
                images += ShowImagesEntity(
                    showId = 0,
                    path = tmdbImageUrlProvider.getPosterUrl(it.poster_path!!, 780),
                    isPrimary = true,
                    type = ImageType.POSTER
                )
            }
            if (it.backdrop_path != null) {
                images += ShowImagesEntity(
                    showId = 0,
                    path = tmdbImageUrlProvider.getBackdropUrl(it.backdrop_path!!, 1280),
                    isPrimary = true,
                    type = ImageType.BACKDROP
                )
            }
            show to images
        }
}