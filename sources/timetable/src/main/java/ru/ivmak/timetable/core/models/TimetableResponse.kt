package ru.ivmak.timetable.core.models

import java.util.Date

data class TimetableResponse (
    val group: String,
    val type: String,
    val name: String,
    val weeks: List<Int>,
    val days: List<DayTimetable>
)

fun Timetable.parseTimeTable(): List<DayTimetable> {
    return this.table.subList(2, this.table.size)
        .mapIndexed { i, strings ->
            DayTimetable(
                this.group,
                this.week,
                i,
                strings[0],
                strings.subList(1, strings.size).mapIndexed { i, lesson ->
                    DayTimetable.Lesson(
                        this.table[0][i + 1],
                        this.table[1][i + 1],
                        lesson
                    )
                },
                Date()
            )
        }
}
