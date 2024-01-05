package ru.ivmak.timetable.ui.models

data class UiDayTimetable(
    val date: String,
    val lessons: List<Lesson>
) {
    data class Lesson (
        val order: String,
        val time: String,
        val lesson: String
    )
}
