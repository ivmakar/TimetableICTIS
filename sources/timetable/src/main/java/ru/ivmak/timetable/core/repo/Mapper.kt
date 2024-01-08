package ru.ivmak.timetable.core.repo

import ru.ivmak.timetable.core.db.models.DayDTO
import ru.ivmak.timetable.core.db.models.DayTimetableDTO
import ru.ivmak.timetable.core.db.models.LessonDTO
import ru.ivmak.timetable.core.models.DayTimetable
import ru.ivmak.timetable.core.models.Timetable
import java.util.Date

private const val PATTERN = """(\d+):(\d+):(.+)"""

fun DayTimetableDTO.toDayTimetable(): DayTimetable? {
    val parsedId = id.parseId()?: return null

    return DayTimetable(
        group = parsedId.third,
        week = parsedId.first,
        dayOfWeek = parsedId.second,
        date = date,
        lessons = lessons.map { it.toLesson() },
        lastUpdated = lastUpdated
    )
}

fun DayTimetable.toDayDTO(): DayDTO {
    val id = "$week:$dayOfWeek:$group"

    return DayDTO(
        id = id,
        group = group,
        date = date,
        lastUpdated = lastUpdated
    )
}

fun LessonDTO.toLesson(): DayTimetable.Lesson {
    return DayTimetable.Lesson(
        order = order,
        time = time,
        lesson = lesson
    )
}

fun DayTimetable.Lesson.toLessonDTO(id: String): LessonDTO {
    return LessonDTO(
        id = id,
        order = order,
        time = time,
        lesson = lesson
    )
}

private fun String.parseId(): Triple<Int, Int, String>? {
    val parsed = PATTERN.toRegex().find(this)?.groupValues
    return parsed?.let { Triple(it[1].toInt(), it[2].toInt(), it[3]) }
}

fun Timetable.parseTimeTable(): List<DayTimetable> {
    return this.table.subList(2, this.table.size)
        .mapIndexed { i, strings ->
            DayTimetable(
                this.group,
                this.week,
                i,
                strings[0],
                strings.subList(1, strings.size).mapIndexed { index, lesson ->
                    DayTimetable.Lesson(
                        this.table[0][index + 1],
                        this.table[1][index + 1],
                        lesson
                    )
                },
                Date()
            )
        }
}
