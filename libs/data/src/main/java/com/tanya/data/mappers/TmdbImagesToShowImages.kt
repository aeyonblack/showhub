package com.tanya.data.mappers

import com.tanya.data.entities.ImageType
import com.tanya.data.entities.ImageType.*
import com.tanya.data.entities.ShowImagesEntity
import com.uwetrottmann.tmdb2.entities.Image
import com.uwetrottmann.tmdb2.entities.TvShow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Map images from the [TvShow] API to our local [ShowImagesEntity]
 */
@Singleton
class TmdbImagesToShowImages @Inject constructor() : Mapper<TvShow, List<ShowImagesEntity>> {

    override suspend fun map(from: TvShow): List<ShowImagesEntity> {

        /*create new collection of show image entities*/
        val results = ArrayList<ShowImagesEntity>()

        /*map images from TvShow to the empty results array we just created*/
        from.images?.posters?.mapTo(results) {
            from.mapImage(it, POSTER)
        }
        from.images?.backdrops?.mapTo(results) {
            from.mapImage(it, BACKDROP)
        }

        /*Synthesize a result that contains images */
        if (results.isEmpty()) {

            from.poster_path?.also {
                results += ShowImagesEntity(
                    showId = 0,
                    type = POSTER,
                    path = it,
                    isPrimary = true
                )
            }
            from.backdrop_path?.also {
                results += ShowImagesEntity(
                    showId = 0,
                    type = BACKDROP,
                    path = it,
                    isPrimary = true
                )
            }
        }

        /*return the result containing images*/
        return results
    }

    private fun TvShow.mapImage(image: Image, type: ImageType) = ShowImagesEntity(
        showId = 0,
        path = image.file_path!!,
        type = type,
        language = image.iso_639_1,
        rating = image.vote_average?.toFloat() ?: 0f,
        isPrimary = when(type) {
            BACKDROP -> image.file_path == backdrop_path
            POSTER -> image.file_path == poster_path
            else -> false
        }
    )
}