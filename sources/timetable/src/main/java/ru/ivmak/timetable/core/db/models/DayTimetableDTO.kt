package ru.ivmak.timetable.core.db.models

import androidx.room.Relation
import java.util.Date

data class DayTimetableDTO(
    val id: String,
    val date: Date,
    val group: String,
    val lastUpdated: Date,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val lessons: List<LessonDTO>
)
