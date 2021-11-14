package com.tanya.data.entities

import androidx.room.*

/**
 * Contains show images from TMDB
 */
@Entity(
    tableName = "show_images",
    indices = [
        Index(value = ["show_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = ShowEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("show_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class ShowImagesEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "show_id") val showId: Long,
    @ColumnInfo(name = "path") override val path: String,
    @ColumnInfo(name = "type") override val type: ImageType,
    @ColumnInfo(name = "lang") override val language: String? = null,
    @ColumnInfo(name = "rating") override val rating: Float = 0f,
    @ColumnInfo(name = "is_primary") override val isPrimary: Boolean = false
): BaseEntity, TmdbImageEntity