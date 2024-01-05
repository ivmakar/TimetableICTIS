package ru.ivmak.search.core.mvi

import ru.ivmak.core.mvi.BaseState
import ru.ivmak.search.ui.models.UiChoice

data class State(
    val searchQuery: String = "",
    val items: List<UiChoice> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) : BaseState
