package ru.ivmak.timetable.core.mvi

import ru.ivmak.core.mvi.BaseAction
import java.util.Date

sealed class Action: BaseAction {
    data class LoadTimetable(val week: Int) : Action()
    data class UpdateTimetable(val week: Int) : Action()
    data class SelectDate(val date: Date) : Action()
    data object Init : Action()
}
