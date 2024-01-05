package ru.ivmak.timetable.core.mvi

import ru.ivmak.core.mvi.BaseState
import ru.ivmak.timetable.ui.models.TableState

data class State(
    val isLoading: Boolean = true,
    val type: String = "",
    val name: String = "",
    val dayTables: List<TableState> = listOf()
) : BaseState
