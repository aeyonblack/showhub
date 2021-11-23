package com.tanya.data.results

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.tanya.data.entities.*
import java.util.*

class RelatedShowEntryWithShow : EntryWithShow<RelatedShowEntity> {

    @Embedded
    override lateinit var entry: RelatedShowEntity

    @Relation(parentColumn = "other_show_id", entityColumn = "id")
    override lateinit var relations: List<ShowEntity>

    @Relation(parentColumn = "other_show_id", entityColumn = "show_id")
    override lateinit var images: List<ShowImagesEntity>

    @delegate:Ignore
    val backdrop by lazy(LazyThreadSafetyMode.NONE) {
        images.findHighestRatedBackdrop()
    }

    @delegate:Ignore
    override val poster by lazy(LazyThreadSafetyMode.NONE) {
        images.findHighestRatedPoster()
    }

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is RelatedShowEntryWithShow -> {
            entry == other.entry && relations == other.relations && images == other.images
        }
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(entry, relations, images)
}