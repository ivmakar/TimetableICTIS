package ru.ivmak.timetable.core.network.models

data class ResponseDTO(
    val table: TimetableDTO,
    val weeks: List<Int>
)
