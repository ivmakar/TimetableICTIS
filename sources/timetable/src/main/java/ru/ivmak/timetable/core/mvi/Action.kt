package ru.ivmak.timetable.core.mvi

import ru.ivmak.core.mvi.BaseAction

sealed class Action: BaseAction {
    data class UpdateTimetable(val week: Int) : Action()
    object Init : Action()
}
