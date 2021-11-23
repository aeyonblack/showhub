package com.tanya.data.results

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.tanya.data.entities.*

class FollowedShowEntryWithShow : EntryWithShow<FollowedShowEntity> {

    @Embedded
    override lateinit var entry: FollowedShowEntity

    @Relation(parentColumn = "show_id", entityColumn = "id")
    override lateinit var relations: List<ShowEntity>

    @Relation(parentColumn = "show_id", entityColumn = "show_id")
    override lateinit var images: List<ShowImagesEntity>

    /*TODO - Add watch stats*/

    @delegate:Ignore
    val backdrop: ShowImagesEntity? by lazy(LazyThreadSafetyMode.NONE) {
        images.findHighestRatedBackdrop()
    }

    @delegate:Ignore
    override val poster: ShowImagesEntity? by lazy(LazyThreadSafetyMode.NONE) {
        images.findHighestRatedPoster()
    }
}