package ru.ivmak.timetable.core.models

data class Response (
    val table: Timetable,
    val weeks: List<Int>,
    val source: Source
)

enum class Source {
    DATABASE, NETWORK
}
