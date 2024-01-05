package ru.ivmak.timetable.core.models

data class Timetable (
    val type: String,
    val name: String,
    val week: Int,
    val group: String,
    val table: List<List<String>>
)
