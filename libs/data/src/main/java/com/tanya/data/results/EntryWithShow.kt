package com.tanya.data.results

import com.tanya.data.Entry
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity
import java.util.*

interface EntryWithShow<ET : Entry> {
    var entry: ET
    var relations: List<ShowEntity>
    var images: List<ShowImagesEntity>

    val show: ShowEntity
        get() {
            check(relations.size == 1)
            return relations[0]
        }

    val poster: ShowImagesEntity?

    fun generateStableId() = Objects.hash(entry::class.java.name, entry.showId).toLong()
}