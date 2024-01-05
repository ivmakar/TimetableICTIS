package ru.ivmak.timetable.core.network.models

import ru.ivmak.timetable.core.models.Timetable

data class TimetableDTO(
    val type: String,
    val name: String,
    val week: Int,
    val group: String,
    val table: List<List<String>>
)

fun TimetableDTO.toTimetable(): Timetable =
    Timetable(
        this.type,
        this.name,
        this.week,
        this.group,
        this.table
    )
