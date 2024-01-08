package ru.ivmak.timetable.core.db.models

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "lesson",
    primaryKeys = ["id", "order"],
    foreignKeys = [
        ForeignKey(
            entity = DayDTO::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("id"),
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class LessonDTO (
    val id: String,
    val order: String,
    val time: String,
    val lesson: String
)