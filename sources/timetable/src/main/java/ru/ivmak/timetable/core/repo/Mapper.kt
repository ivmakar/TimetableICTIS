package ru.ivmak.timetable.core.repo

import ru.ivmak.core.utils.getCalendarOfFirstWeek
import ru.ivmak.core.utils.toParsableDate
import ru.ivmak.timetable.core.db.models.DayDTO
import ru.ivmak.timetable.core.db.models.DayTimetableDTO
import ru.ivmak.timetable.core.db.models.LessonDTO
import ru.ivmak.timetable.core.models.DayTimetable
import ru.ivmak.timetable.core.models.Timetable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


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

private fun String.parseDate(week: Int, day: Int): Date {
    val formatter = SimpleDateFormat("EEE,d MMMM", Locale.forLanguageTag("ru-RU"))
    val date = formatter.parse(this.toParsableDate())
    if (date == null) {
        val calendar = Date().getCalendarOfFirstWeek()
        calendar.add(Calendar.DAY_OF_YEAR, (week - 1) * 7 + day)
        return calendar.time
    }
    val curCalendar = Calendar.getInstance()
    curCalendar.time = Date()
    val calendar = Calendar.getInstance()
    calendar.time = date
    if (curCalendar.get(Calendar.MONTH) in Calendar.FEBRUARY..Calendar.AUGUST) {
        calendar.set(Calendar.YEAR, curCalendar.get(Calendar.YEAR))
    } else {
        when {
            calendar.get(Calendar.MONTH) >= Calendar.AUGUST
                    && curCalendar.get(Calendar.MONTH) in Calendar.JANUARY..Calendar.FEBRUARY -> calendar.set(Calendar.YEAR, curCalendar.get(Calendar.YEAR) - 1)
            calendar.get(Calendar.MONTH) in Calendar.JANUARY..Calendar.FEBRUARY
                    && curCalendar.get(Calendar.MONTH) >= Calendar.AUGUST -> calendar.set(Calendar.YEAR, curCalendar.get(Calendar.YEAR) + 1)
            else -> calendar.set(Calendar.YEAR, curCalendar.get(Calendar.YEAR))
        }
    }
    return calendar.time
}

private fun String?.getNextParsedDate(week: Int, day: Int): Date {
    val date = (this?: "").parseDate(week, day)
    date.time += 1000*60*60*24
    return date
}

fun Timetable.parseTimeTable(): List<DayTimetable> {
    return (0..6).toList()
        .map { i ->
            DayTimetable(
                group = this.group,
                week = this.week,
                dayOfWeek = i,
                date = this.table.getOrNull(i + 2)?.getOrNull(0)?.parseDate(this.week, i)?: this.table.getOrNull(i + 1)?.getOrNull(0).getNextParsedDate(this.week, i - 1),
                lessons = List(this.table[0].subList(1, this.table[0].size).size) { index ->
                    DayTimetable.Lesson(
                        this.table[0][index + 1],
                        this.table[1][index + 1],
                        this.table.getOrNull(i + 2)?.getOrNull(index + 1)?: ""
                    )
                },
                lastUpdated = Date()
            )
        }
}
