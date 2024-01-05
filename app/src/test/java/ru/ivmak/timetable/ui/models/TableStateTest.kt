package ru.ivmak.timetable.ui.models

import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.ivmak.timetable.core.network.models.ResponseDTO

class TableStateTest {

    private val jsonData = "{\"table\": {\"type\": \"Расписание занятий учебной группы\", \"name\": \"КТбо4-7\", \"week\": 1, \"group\": \"134.html\", \"table\": [[\"Пары\", \"1-я\", \"2-я\", \"3-я\", \"4-я\", \"5-я\", \"6-я\", \"7-я\"], [\"Время\", \"08:00-09:35\", \"09:50-11:25\", \"11:55-13:30\", \"13:45-15:20\", \"15:50-17:25\", \"17:40-19:15\", \"19:30-21:05\"], [\"Пнд,28 августа\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"], [\"Втр,29 августа\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"], [\"Срд,30 августа\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"], [\"Чтв,31 августа\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"], [\"Птн,01  сентября\", \"пр.Дисциплины ВПК Вакансия ИКТИБ +\", \"пр.Дисциплины ВПК Вакансия ИКТИБ +\", \"пр.Дисциплины ВПК Вакансия ИКТИБ +\", \"пр.Дисциплины ВПК Вакансия ИКТИБ +\", \"пр.Дисциплины ВПК Вакансия ИКТИБ +\", \"пр.Дисциплины ВПК Вакансия ИКТИБ +2\", \"\"], [\"Сбт,02  сентября\", \"\", \"лек.Программирование компьтерной графики Чефранов А. Г. LMS-1\", \"лек.Теория языков программирования и вычислительных процессов Чефранов А. Г. LMS-1\", \"\", \"\", \"\", \"\"]], \"link\": \"КТбо4-7\"}, \"weeks\": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 20, 21, 22, 23]}"

    @Test
    fun `Test parse timetable`() {
        val timetableDTO = Gson().fromJson(jsonData, ResponseDTO::class.java)
        val table = timetableDTO.table.table
        val expected = listOf(
            UiDayTimetable(
                "Пнд,28 августа",
                listOf(
                    UiDayTimetable.Lesson("1-я", "08:00-09:35", ""),
                    UiDayTimetable.Lesson("2-я", "09:50-11:25", ""),
                    UiDayTimetable.Lesson("3-я", "11:55-13:30", ""),
                    UiDayTimetable.Lesson("4-я", "13:45-15:20", ""),
                    UiDayTimetable.Lesson("5-я", "15:50-17:25", ""),
                    UiDayTimetable.Lesson("6-я", "17:40-19:15", ""),
                    UiDayTimetable.Lesson("7-я", "19:30-21:05", ""),
                )
            ),
            UiDayTimetable(
                "Втр,29 августа",
                listOf(
                    UiDayTimetable.Lesson("1-я", "08:00-09:35", ""),
                    UiDayTimetable.Lesson("2-я", "09:50-11:25", ""),
                    UiDayTimetable.Lesson("3-я", "11:55-13:30", ""),
                    UiDayTimetable.Lesson("4-я", "13:45-15:20", ""),
                    UiDayTimetable.Lesson("5-я", "15:50-17:25", ""),
                    UiDayTimetable.Lesson("6-я", "17:40-19:15", ""),
                    UiDayTimetable.Lesson("7-я", "19:30-21:05", ""),
                )
            ),
            UiDayTimetable(
                "Срд,30 августа",
                listOf(
                    UiDayTimetable.Lesson("1-я", "08:00-09:35", ""),
                    UiDayTimetable.Lesson("2-я", "09:50-11:25", ""),
                    UiDayTimetable.Lesson("3-я", "11:55-13:30", ""),
                    UiDayTimetable.Lesson("4-я", "13:45-15:20", ""),
                    UiDayTimetable.Lesson("5-я", "15:50-17:25", ""),
                    UiDayTimetable.Lesson("6-я", "17:40-19:15", ""),
                    UiDayTimetable.Lesson("7-я", "19:30-21:05", ""),
                )
            ),
            UiDayTimetable(
                "Чтв,31 августа",
                listOf(
                    UiDayTimetable.Lesson("1-я", "08:00-09:35", ""),
                    UiDayTimetable.Lesson("2-я", "09:50-11:25", ""),
                    UiDayTimetable.Lesson("3-я", "11:55-13:30", ""),
                    UiDayTimetable.Lesson("4-я", "13:45-15:20", ""),
                    UiDayTimetable.Lesson("5-я", "15:50-17:25", ""),
                    UiDayTimetable.Lesson("6-я", "17:40-19:15", ""),
                    UiDayTimetable.Lesson("7-я", "19:30-21:05", ""),
                )
            ),
            UiDayTimetable(
                "Птн,01  сентября",
                listOf(
                    UiDayTimetable.Lesson("1-я", "08:00-09:35", "пр.Дисциплины ВПК Вакансия ИКТИБ +"),
                    UiDayTimetable.Lesson("2-я", "09:50-11:25", "пр.Дисциплины ВПК Вакансия ИКТИБ +"),
                    UiDayTimetable.Lesson("3-я", "11:55-13:30", "пр.Дисциплины ВПК Вакансия ИКТИБ +"),
                    UiDayTimetable.Lesson("4-я", "13:45-15:20", "пр.Дисциплины ВПК Вакансия ИКТИБ +"),
                    UiDayTimetable.Lesson("5-я", "15:50-17:25", "пр.Дисциплины ВПК Вакансия ИКТИБ +"),
                    UiDayTimetable.Lesson("6-я", "17:40-19:15", "пр.Дисциплины ВПК Вакансия ИКТИБ +2"),
                    UiDayTimetable.Lesson("7-я", "19:30-21:05", ""),
                )
            ),
            UiDayTimetable(
                "Сбт,02  сентября",
                listOf(
                    UiDayTimetable.Lesson("1-я", "08:00-09:35", ""),
                    UiDayTimetable.Lesson("2-я", "09:50-11:25", "лек.Программирование компьтерной графики Чефранов А. Г. LMS-1"),
                    UiDayTimetable.Lesson("3-я", "11:55-13:30", "лек.Теория языков программирования и вычислительных процессов Чефранов А. Г. LMS-1"),
                    UiDayTimetable.Lesson("4-я", "13:45-15:20", ""),
                    UiDayTimetable.Lesson("5-я", "15:50-17:25", ""),
                    UiDayTimetable.Lesson("6-я", "17:40-19:15", ""),
                    UiDayTimetable.Lesson("7-я", "19:30-21:05", ""),
                )
            ),
        )

        val result = table.parseTimeTable()

        assertEquals(expected, result)
    }
}
