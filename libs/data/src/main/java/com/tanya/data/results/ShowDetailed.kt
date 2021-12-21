package com.tanya.data.results

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity
import com.tanya.data.entities.findHighestRatedBackdrop
import com.tanya.data.entities.findHighestRatedPoster
import java.util.*

class ShowDetailed {

    @Embedded
    lateinit var show: ShowEntity

    @Relation(parentColumn = "id", entityColumn = "show_id")
    lateinit var images: List<ShowImagesEntity>

    @delegate:Ignore
    val backdrop: ShowImagesEntity? by lazy(LazyThreadSafetyMode.NONE) {
        images.findHighestRatedBackdrop()
    }

    @delegate:Ignore
    val poster: ShowImagesEntity? by lazy(LazyThreadSafetyMode.NONE) {
        images.findHighestRatedPoster()
    }

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is ShowDetailed -> show == other.show && images == other.images
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(show, images)
}