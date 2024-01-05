package ru.ivmak.search.core.mvi

import ru.ivmak.search.core.models.Choice

sealed class Change {
    object Loading : Change()
    data class ListLoaded(val items: List<Choice>) : Change()
    data class QueryChanged(val query: String) : Change()
    object Error: Change()
}
