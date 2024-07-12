package com.example.feature.data.local.model

import androidx.room.TypeConverter
import java.util.Date


/**
 * Type converter for Date objects to Long timestamps and vice versa.
 */
class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}