package ru.ivmak.core.utils

import androidx.room.TypeConverter
import com.google.gson.Gson

class ListIntTypeConverter {

    @TypeConverter
    fun fromTimestamp(value: String): List<Int> {
        return Gson().fromJson(value, ArrayList::class.java) as ArrayList<Int>
    }

    @TypeConverter
    fun dateToTimestamp(value: List<Int>): String {
        return Gson().toJson(value)
    }


}
