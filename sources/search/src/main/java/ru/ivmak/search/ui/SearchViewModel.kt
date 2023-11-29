package ru.ivmak.search.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import ru.ivmak.core.mvi.BaseViewModel
import ru.ivmak.core.mvi.Reducer
import ru.ivmak.core.utils.DataResponse
import ru.ivmak.search.core.mvi.Action
import ru.ivmak.search.core.mvi.Change
import ru.ivmak.search.core.mvi.State
import ru.ivmak.search.core.repo.SearchRepo
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repo: SearchRepo
) : BaseViewModel<Action, State>() {

    override val initialState = State()

    private val reducer: Reducer<State, Change> = { state, change ->
        Log.d(TAG, "Received Change: $change\n$state")
        when (change) {
            is Change.Loading -> state.copy(isLoading = state.items.isEmpty(), isError = false)
            is Change.ListLoaded -> state.copy(
                isError = false,
                isLoading = false,
                items = change.items,
            )

            is Change.Error -> state.copy(
                items = listOf(),
                isLoading = false,
                isError = true,
            )

            is Change.QueryChanged -> state.copy(searchQuery = change.query)
        }
    }

    init {
        bindActions()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun bindActions() {
        viewModelScope.launch {

            val searchQuery = actions
                .filterIsInstance<Action.SearchQueryChanged>()
                .flatMapLatest { buildSearchAction(it) }

            val searchQueryChanged = searchQuery
                .filterIsInstance<Action.SearchQueryChanged>()
                .map { Change.QueryChanged(it.query) }

            val itemsLoadedChange = searchQuery
                .filterIsInstance<Action.LoadChoices>()
                .flatMapLatest { repo.queryTimetables(it.query) }
                .map {
                    when(it) {
                        is DataResponse.Error -> Change.Error
                        is DataResponse.Success -> Change.ListLoaded(it.data)
                        is DataResponse.Loading -> Change.Loading
                    }
                }

            val allChanges = merge(searchQueryChanged, itemsLoadedChange)

            allChanges
                .scan(initialState, reducer)
                .distinctUntilChanged()
                .collect {
                    state.value = it
                    Log.d(TAG, "State updated: $it")
                }
        }
    }

    private fun buildSearchAction(action: Action.SearchQueryChanged): Flow<Action> = flow {
        emit(action)
        if (action.query.isNotEmpty()) {
            delay(500)
        }
        emit(Action.LoadChoices(action.query))
    }

}
