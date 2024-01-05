package ru.ivmak.timetable.core.mvi

import ru.ivmak.timetable.core.models.Response

sealed class Change {
    data class Loading(val week: Int): Change()
    data class Error(val week: Int, val e: Exception?): Change()
    data class TimetableLoaded(val response: Response): Change()
}
