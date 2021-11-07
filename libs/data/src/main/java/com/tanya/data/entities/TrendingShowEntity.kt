package com.tanya.data.entities

import androidx.room.*
import com.tanya.data.PaginatedEntry

@Entity(
    tableName = "trending_shows",
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
data class TrendingShowEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "show_id") override val showId: Long,
    @ColumnInfo(name = "page") override val page: Int,
    @ColumnInfo(name = "watchers") val watchers: Int
) : PaginatedEntry