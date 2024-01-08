package ru.ivmak.timetable.core.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "day")
data class DayDTO(
    @PrimaryKey
    val id: String,
    val group: String,
    val date: String,
    val lastUpdated: Date
)
