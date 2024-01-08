package ru.ivmak.timetable.core.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupDTO(
    @PrimaryKey
    val group: String,
    val type: String,
    val name: String,
    val weeks: List<Int>
)
