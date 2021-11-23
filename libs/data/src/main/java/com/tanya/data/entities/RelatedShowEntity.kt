package com.tanya.data.entities

import androidx.room.*
import com.tanya.data.MultipleEntry

@Entity(
    tableName = "related_shows",
    indices = [
        Index(value = ["show_id"]),
        Index(value = ["other_show_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = ShowEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("show_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ShowEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("other_show_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RelatedShowEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "show_id") override val showId: Long,
    @ColumnInfo(name = "other_show_id") override val otherShowId: Long,
    @ColumnInfo(name = "order_index") val orderIndex: Int
): MultipleEntry