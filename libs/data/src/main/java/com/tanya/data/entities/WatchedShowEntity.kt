package com.tanya.data.entities

import androidx.room.*
import com.tanya.data.Entry
import org.threeten.bp.OffsetDateTime

@Entity(
    tableName = "watched_shows",
    indices = [
        Index(value = ["show_id"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = ShowEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("show_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WatchedShowEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "show_id") override val showId: Long,
    @ColumnInfo(name = "last_watched") val lastWatched: OffsetDateTime
): Entry