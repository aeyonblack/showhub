package com.tanya.data.android.repository.database

import androidx.room.TypeConverter
import com.tanya.data.entities.ImageType

object TypeConverters {

    private val imageTypeValues by lazy(LazyThreadSafetyMode.NONE) {ImageType.values()}

    @TypeConverter
    @JvmStatic
    fun fromImageType(value: ImageType) = value.storageKey

    @TypeConverter
    @JvmStatic
    fun toImageType(value: String?) = imageTypeValues.firstOrNull { it.storageKey == value }

}