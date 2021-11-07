package com.tanya.data.results

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.tanya.data.entities.*
import com.tanya.data.entities.findHighestRatedBackdrop
import java.util.*

class PopularEntryWithShow : EntryWithShow<PopularShowEntity>{
    @Embedded
    override lateinit var entry: PopularShowEntity

    @Relation(parentColumn = "show_id", entityColumn = "id")
    override lateinit var relations: List<ShowEntity>

    @Relation(parentColumn = "show_id", entityColumn = "show_id")
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
        other is PopularEntryWithShow -> {
            entry == other.entry && relations == other.relations && images == other.images
        }
        else -> false
    }

    override fun hashCode() = Objects.hash(entry, relations, images)
}