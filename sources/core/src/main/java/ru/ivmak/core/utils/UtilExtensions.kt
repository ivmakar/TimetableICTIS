package ru.ivmak.core.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Date.getCalendarOfFirstWeek(): Calendar {
    val calendarOfFirstWeek = Calendar.getInstance()
    calendarOfFirstWeek.time = this

    val isSecondSemester = calendarOfFirstWeek.get(Calendar.MONTH) in Calendar.FEBRUARY..Calendar.AUGUST

    if (isSecondSemester) {
        calendarOfFirstWeek.set(Calendar.MONTH, Calendar.FEBRUARY)
        calendarOfFirstWeek.set(Calendar.DAY_OF_MONTH, 9)
        when(calendarOfFirstWeek.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> calendarOfFirstWeek.set(Calendar.DAY_OF_MONTH, 3)
            Calendar.TUESDAY -> calendarOfFirstWeek.set(Calendar.DAY_OF_MONTH, 8)
            Calendar.WEDNESDAY -> calendarOfFirstWeek.set(Calendar.DAY_OF_MONTH, 7)
            Calendar.THURSDAY -> calendarOfFirstWeek.set(Calendar.DAY_OF_MONTH, 6)
            Calendar.FRIDAY -> calendarOfFirstWeek.set(Calendar.DAY_OF_MONTH, 5)
            Calendar.SATURDAY -> calendarOfFirstWeek.set(Calendar.DAY_OF_MONTH, 4)
        }
    } else {
        calendarOfFirstWeek.set(
            if (calendarOfFirstWeek.get(Calendar.MONTH) == Calendar.JANUARY)
                calendarOfFirstWeek.get(Calendar.YEAR) - 1
            else
                calendarOfFirstWeek.get(Calendar.YEAR),
            Calendar.SEPTEMBER,
            1
        )

        when(calendarOfFirstWeek.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> calendarOfFirstWeek.set(
                Calendar.DAY_OF_YEAR, calendarOfFirstWeek.get(
                    Calendar.DAY_OF_YEAR) + 1)
            Calendar.TUESDAY -> calendarOfFirstWeek.set(
                Calendar.DAY_OF_YEAR, calendarOfFirstWeek.get(
                    Calendar.DAY_OF_YEAR) - 1)
            Calendar.WEDNESDAY -> calendarOfFirstWeek.set(
                Calendar.DAY_OF_YEAR, calendarOfFirstWeek.get(
                    Calendar.DAY_OF_YEAR) - 2)
            Calendar.THURSDAY -> calendarOfFirstWeek.set(
                Calendar.DAY_OF_YEAR, calendarOfFirstWeek.get(
                    Calendar.DAY_OF_YEAR) - 3)
            Calendar.FRIDAY -> calendarOfFirstWeek.set(
                Calendar.DAY_OF_YEAR, calendarOfFirstWeek.get(
                    Calendar.DAY_OF_YEAR) - 4)
            Calendar.SATURDAY -> calendarOfFirstWeek.set(
                Calendar.DAY_OF_YEAR, calendarOfFirstWeek.get(
                    Calendar.DAY_OF_YEAR) - 5)
        }
    }
    return calendarOfFirstWeek
}

fun Date.getCurDayOfSemester(): Int {
    val calendarOfFirstWeek = this.getCalendarOfFirstWeek()

    return ((this.time / (1000*60*60*24)) - (calendarOfFirstWeek.time.time / (1000*60*60*24))).toInt()
}

fun Date.toReadableText(): String {
    val formatter = SimpleDateFormat("EEE, d MMMM", Locale.forLanguageTag("ru-RU"))
    return formatter.format(this)
}

fun String.toParsableDate(): String {
    var str = this.replace("Пнд", "пн")
    str = str.replace("Втр", "вт")
    str = str.replace("Срд", "ср")
    str = str.replace("Чтв", "чт")
    str = str.replace("Птн", "пт")
    str = str.replace("Сбт", "сб")
    str = str.replace("Вск", "вс")
    str = str.replace("  ", " ")

    return str
}
