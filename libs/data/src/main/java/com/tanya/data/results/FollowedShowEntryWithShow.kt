package com.tanya.data.results

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.tanya.data.entities.*
import java.util.*

class FollowedShowEntryWithShow : EntryWithShow<FollowedShowEntity> {

    @Embedded
    override lateinit var entry: FollowedShowEntity

    @Relation(parentColumn = "show_id", entityColumn = "id")
    override lateinit var relations: List<ShowEntity>

    @Relation(parentColumn = "show_id", entityColumn = "show_id")
    override lateinit var images: List<ShowImagesEntity>

    /*TODO - Add watch stats later*/

    @delegate:Ignore
    val backdrop: ShowImagesEntity? by lazy(LazyThreadSafetyMode.NONE) {
        images.findHighestRatedBackdrop()
    }

    @delegate:Ignore
    override val poster: ShowImagesEntity? by lazy(LazyThreadSafetyMode.NONE) {
        images.findHighestRatedPoster()
    }

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is FollowedShowEntryWithShow -> {
            entry == other.entry && relations == other.relations && images == other.images
        }
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(entry, relations, images)
}