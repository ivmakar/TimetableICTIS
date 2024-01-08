package ru.ivmak.timetable.ui.models

import ru.ivmak.timetable.core.models.DayTimetable

sealed class TableState(open val week: Int, open val order: Int) {
    data class Loading(override val week: Int, override val order: Int): TableState(week, order)

    data class Error(override val week: Int, override val order: Int, val e: Exception?): TableState(week, order)

    data class LoadedTimetable(override val week: Int, override val order: Int, val table: DayTimetable, val isLoading: Boolean = false): TableState(week, order)
}

fun List<TableState>.updateForErrorResponse(week: Int, e: Exception?): List<TableState> {
    return this.map {
        if (it.week == week) {
            TableState.Error(week, it.order, e)
        } else {
            it
        }
    }
}

fun List<TableState>.updateForLoadingResponse(week: Int): List<TableState> {
    return this.map {
        when {
            it.week == week && it is TableState.LoadedTimetable -> it.copy(isLoading = true)
            it.week == week -> TableState.Loading(week, it.order)
            else -> it
        }
    }
}

fun List<TableState>.updateForLoaded(days: List<DayTimetable>): List<TableState> {
    return this.map { state ->
        val day = days.find { it.week == state.week && it.dayOfWeek == state.order }
        if (day != null) {
            TableState.LoadedTimetable(day.week, day.dayOfWeek, day)
        } else {
            state
        }
    }
}
