package ru.ivmak.search.core.mvi

import ru.ivmak.core.mvi.BaseState
import ru.ivmak.search.core.network.models.ChoiceList

data class State(
    val searchQuery: String = "",
    val items: List<ChoiceList.Choice> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) : BaseState
