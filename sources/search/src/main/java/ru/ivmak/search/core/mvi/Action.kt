package ru.ivmak.search.core.mvi

import ru.ivmak.core.mvi.BaseAction

sealed class Action: BaseAction {
    data class SearchQueryChanged(val query: String) : Action()
    data class LoadChoices(val query: String) : Action()
}
