package ru.ivmak.timetable.core.models

import java.util.Date

data class DayTimetable(
    val group: String,
    val week: Int,
    val dayOfWeek: Int,
    val date: Date,
    val lessons: List<Lesson>,
    val lastUpdated: Date
) {
    data class Lesson (
        val order: String,
        val time: String,
        val lesson: String
    )
}
