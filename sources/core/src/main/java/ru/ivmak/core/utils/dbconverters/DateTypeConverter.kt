package ru.ivmak.core.utils.dbconverters

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConverter {

    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }


}