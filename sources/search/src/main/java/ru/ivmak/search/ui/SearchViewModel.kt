package ru.ivmak.search.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
import ru.ivmak.search.ui.models.UiChoice
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repo: SearchRepo
) : ViewModel(), BaseViewModel<Action, State> {

    private val _actions = MutableSharedFlow<Action>()
    override val actions: SharedFlow<Action>
        get() = _actions.asSharedFlow()

    private val initialState = State()
    private val state = MutableStateFlow(initialState)
    override val observableState: StateFlow<State>
        get() = state.asStateFlow()

    override fun dispatch(action: Action) {
        viewModelScope.launch {
            Log.d(TAG, "Received Action: $action")
            _actions.emit(action)
        }
    }

    private val reducer: Reducer<State, Change> = { state, change ->
        Log.d(TAG, "Received Change: $change\n$state")
        when (change) {
            is Change.Loading -> state.copy(isLoading = state.items.isEmpty(), isError = false)
            is Change.ListLoaded -> state.copy(
                isError = false,
                isLoading = false,
                items = change.items.map { UiChoice(it.name, it.group) },
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
                .flatMapLatest { repo.queryTimetables(it.query.trim()) }
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

    companion object {
        private const val TAG = "SearchViewModel"
    }

}
