package ru.ivmak.timetable.core.repo

import org.junit.Assert
import org.junit.Test
import ru.ivmak.timetable.core.db.models.DayDTO
import ru.ivmak.timetable.core.db.models.DayTimetableDTO
import ru.ivmak.timetable.core.db.models.LessonDTO
import ru.ivmak.timetable.core.models.DayTimetable
import java.util.Date


class MapperKtTest {
    private val lastUpdated = Date()

    private val dayTimetable = DayTimetable(
        group = "134.html",
        week = 1,
        dayOfWeek = 0,
        date = "Пнд,28 августа",
        lessons = listOf(
            DayTimetable.Lesson("1-я", "08:00-09:35", ""),
            DayTimetable.Lesson("2-я", "09:50-11:25", ""),
            DayTimetable.Lesson("3-я", "11:55-13:30", ""),
            DayTimetable.Lesson("4-я", "13:45-15:20", ""),
            DayTimetable.Lesson("5-я", "15:50-17:25", ""),
            DayTimetable.Lesson("6-я", "17:40-19:15", ""),
            DayTimetable.Lesson("7-я", "19:30-21:05", ""),
        ),
        lastUpdated = lastUpdated
    )

    private val lesson = DayTimetable.Lesson("1-я", "08:00-09:35", "")
    private val lessonDTO = LessonDTO("1:0:134.html", "1-я", "08:00-09:35", "")

    @Test
    fun `Test map DayTimetableDTO to DayTimetable`() {

        val dayTimetableDTO = DayTimetableDTO(
            id = "1:0:134.html",
            date = "Пнд,28 августа",
            lastUpdated = lastUpdated,
            lessons = listOf(
                LessonDTO("1:0:134.html", "1-я", "08:00-09:35", ""),
                LessonDTO("1:0:134.html", "2-я", "09:50-11:25", ""),
                LessonDTO("1:0:134.html", "3-я", "11:55-13:30", ""),
                LessonDTO("1:0:134.html", "4-я", "13:45-15:20", ""),
                LessonDTO("1:0:134.html", "5-я", "15:50-17:25", ""),
                LessonDTO("1:0:134.html", "6-я", "17:40-19:15", ""),
                LessonDTO("1:0:134.html", "7-я", "19:30-21:05", ""),
            )
        )

        val actual = dayTimetableDTO.toDayTimetable()

        Assert.assertEquals(dayTimetable, actual)
    }

    @Test
    fun `Test map DayTimetable to DayTimetableDTO`() {

        val dayDTO = DayDTO(
            id = "1:0:134.html",
            group = "134.html",
            date = "Пнд,28 августа",
            lastUpdated = lastUpdated
        )

        val actual = dayTimetable.toDayDTO()
        Assert.assertEquals(dayDTO, actual)
    }

    @Test
    fun `Test map Lesson to LessonDTO`() {

        val actual = lesson.toLessonDTO("1:0:134.html")
        Assert.assertEquals(lessonDTO, actual)
    }

    @Test
    fun `Test map LessonDTO to Lesson`() {

        val actual = lessonDTO.toLesson()
        Assert.assertEquals(lesson, actual)
    }

}
