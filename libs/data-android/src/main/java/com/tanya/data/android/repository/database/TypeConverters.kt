package com.tanya.data.android.repository.database

import androidx.room.TypeConverter
import com.tanya.data.entities.ImageType
import com.tanya.data.entities.PendingAction
import com.tanya.data.entities.Request
import com.tanya.data.entities.ShowStatus
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter

object ShowhubTypeConverters {

    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    private val requestValues by lazy(LazyThreadSafetyMode.NONE) { Request.values() }
    private val imageTypeValues by lazy(LazyThreadSafetyMode.NONE) { ImageType.values() }
    private val pendingActionValues by lazy(LazyThreadSafetyMode.NONE) { PendingAction.values() }
    private val showStatusValues by lazy(LazyThreadSafetyMode.NONE) { ShowStatus.values() }
    private val dayOfWeekValues by lazy(LazyThreadSafetyMode.NONE) { DayOfWeek.values() }

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?) = value?.let { formatter.parse(value, OffsetDateTime::from) }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? = date?.format(formatter)

    @TypeConverter
    @JvmStatic
    fun toZoneId(value: String?) = value?.let { ZoneId.of(it) }

    @TypeConverter
    @JvmStatic
    fun fromZoneId(value: ZoneId?) = value?.id

    @TypeConverter
    @JvmStatic
    fun toLocalTime(value: String?) = value?.let { LocalTime.parse(value) }

    @TypeConverter
    @JvmStatic
    fun fromLocalTime(value: LocalTime?) = value?.format(DateTimeFormatter.ISO_LOCAL_TIME)

    @TypeConverter
    @JvmStatic
    fun toDayOfWeek(value: Int?): DayOfWeek? {
        return if (value != null) {
            dayOfWeekValues.firstOrNull { it.value == value }
        } else null
    }

    @TypeConverter
    @JvmStatic
    fun fromDayOfWeek(day: DayOfWeek?) = day?.value

    @TypeConverter
    @JvmStatic
    fun toInstant(value: Long?) = value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    @JvmStatic
    fun fromInstant(date: Instant?) = date?.toEpochMilli()

    @TypeConverter
    @JvmStatic
    fun fromPendingAction(action: PendingAction): String = action.value

    @TypeConverter
    @JvmStatic
    fun toPendingAction(action: String?) = pendingActionValues.firstOrNull { it.value == action }

    @TypeConverter
    @JvmStatic
    fun fromRequest(value: Request) = value.tag

    @TypeConverter
    @JvmStatic
    fun toRequest(value: String) = requestValues.firstOrNull { it.tag == value }

    @TypeConverter
    @JvmStatic
    fun fromImageType(value: ImageType) = value.storageKey

    @TypeConverter
    @JvmStatic
    fun toImageType(value: String?) = imageTypeValues.firstOrNull { it.storageKey == value }

    @TypeConverter
    @JvmStatic
    fun fromShowStatus(value: ShowStatus?) = value?.storageKey

    @TypeConverter
    @JvmStatic
    fun toShowStatus(value: String?) = showStatusValues.firstOrNull { it.storageKey == value }

}