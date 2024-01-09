package ru.ivmak.timetable.core.models

data class TimetableResponse (
    val group: String,
    val type: String,
    val name: String,
    val weeks: List<Int>,
    val days: List<DayTimetable>
)
