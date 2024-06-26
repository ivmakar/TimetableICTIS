package ru.ivmak.timetable.core.mvi

import ru.ivmak.timetable.core.models.TimetableResponse
import java.util.Date

sealed class Change {
    data class Loading(val week: Int): Change()
    data class Error(val week: Int, val e: Exception?): Change()
    data class TimetableLoaded(val response: TimetableResponse): Change()
    data class SelectDate(val date: Date): Change()
}
