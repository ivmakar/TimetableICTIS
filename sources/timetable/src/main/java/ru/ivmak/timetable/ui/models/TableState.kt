package ru.ivmak.timetable.ui.models

import ru.ivmak.timetable.core.models.Response
import ru.ivmak.timetable.core.models.Source

sealed class TableState(open val week: Int, open val order: Int) {
    data class Loading(override val week: Int, override val order: Int): TableState(week, order)

    data class Error(override val week: Int, override val order: Int, val e: Exception?): TableState(week, order)

    data class LoadedFromDb(override val week: Int, override val order: Int, val table: UiDayTimetable): TableState(week, order)

    data class LoadedFromNetwork(override val week: Int, override val order: Int, val table: UiDayTimetable): TableState(week, order)
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
        if (it.week == week) {
            TableState.Loading(week, it.order)
        } else {
            it
        }
    }
}

fun List<List<String>>.parseTimeTable(): List<UiDayTimetable> {
    return this.subList(2, this.size)
        .map { strings ->
            UiDayTimetable(
                strings[0],
                strings.subList(1, strings.size).mapIndexed { i, lesson ->
                    UiDayTimetable.Lesson(
                        this[0][i + 1],
                        this[1][i + 1],
                        lesson
                    )
                }
            )
        }
}

fun List<TableState>.updateForLoaded(response: Response): List<TableState> {
    val days = response.table.table.parseTimeTable()
    return this.map {
        if (it.week == response.table.week) {
            when (response.source) {
                Source.DATABASE -> TableState.LoadedFromDb(it.week, it.order, days[it.order])
                Source.NETWORK -> TableState.LoadedFromNetwork(it.week, it.order, days[it.order])
            }
        } else {
            it
        }
    }
}
