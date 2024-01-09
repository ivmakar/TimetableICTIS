package ru.ivmak.timetable.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import ru.ivmak.core.mvi.BaseViewModel
import ru.ivmak.core.mvi.Reducer
import ru.ivmak.core.utils.DataResponse
import ru.ivmak.core.utils.getCurDayOfSemester
import ru.ivmak.timetable.core.models.TimetableResponse
import ru.ivmak.timetable.core.mvi.Action
import ru.ivmak.timetable.core.mvi.Change
import ru.ivmak.timetable.core.mvi.State
import ru.ivmak.timetable.core.repo.TimetableRepo
import ru.ivmak.timetable.ui.models.TableState
import ru.ivmak.timetable.ui.models.updateForErrorResponse
import ru.ivmak.timetable.ui.models.updateForLoaded
import ru.ivmak.timetable.ui.models.updateForLoadingResponse
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repo: TimetableRepo
) : ViewModel(), BaseViewModel<Action, State> {

    private val group: String =
        savedStateHandle.get<String>("group")
            ?: throw IllegalArgumentException("You have to provide group as parameter with type String when navigating to timetable")

    private val _actions = MutableSharedFlow<Action>(replay = 1)
    override val actions: SharedFlow<Action>
        get() = _actions.asSharedFlow()

    private val initialState = State(curDayOfSemester = Date().getCurDayOfSemester())
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
                is Change.Loading -> {
                    if (state.type.isEmpty() && state.name.isEmpty()) {
                        state.copy(isLoading = true)
                    } else {
                        state.copy(dayTables = state.dayTables.updateForLoadingResponse(change.week))
                    }
                }
                is Change.Error -> {
                    if (state.type.isEmpty() && state.name.isEmpty()) {
                        state.copy(isLoading = false)
                    } else {
                        state.copy(dayTables = state.dayTables.updateForErrorResponse(change.week, change.e))
                    }
                }
                is Change.TimetableLoaded -> {
                    val days = if (state.type.isEmpty() && state.name.isEmpty())
                        change.response.weeks.flatMap { week ->
                            (0..6).toList().map { TableState.Loading(week, it) }
                        }.updateForLoaded(change.response.days)
                    else
                        state.dayTables.updateForLoaded(change.response.days)

                    val curDayOfSemester = state.curDayOfSemester / 7 + 1 to state.curDayOfSemester % 7
                    val curPage = days.find { it.week == curDayOfSemester.first && it.dayOfWeek == curDayOfSemester.second }
                        ?: days.find { it.week > curDayOfSemester.first }
                    val initialPage = curPage?.let { days.indexOf(it) }?: 0
                    state.copy(
                        isLoading = false,
                        type = change.response.type,
                        name = change.response.name,
                        dayTables = days,
                        initialPage = initialPage,
                        openPage = if (state.type.isEmpty() && state.name.isEmpty()) initialPage else state.openPage
                    )
                }

                is Change.SelectDate -> {
                    if (state.dayTables.isNotEmpty()) {
                        val day = change.date.getCurDayOfSemester()
                        val curDayOfSemester = day / 7 + 1 to day % 7
                        val curPage = state.dayTables.find { it.week == curDayOfSemester.first && it.dayOfWeek == curDayOfSemester.second }
                            ?: state.dayTables.find { it.week > curDayOfSemester.first }
                        state.copy(openPage = curPage?.let { state.dayTables.indexOf(it) }?: state.initialPage)
                    } else {
                        state
                    }
                }
            }
    }

    init {
        bindActions()
        dispatch(Action.Init)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun bindActions() {
        viewModelScope.launch {

            val updateTimetableChange = actions
                .filterIsInstance<Action.UpdateTimetable>()
                .flatMapLatest { repo.updateTimetable(group, it.week) }
                .map {
                    when(it.second) {
                        is DataResponse.Error -> Change.Error(it.first, (it.second as DataResponse.Error<TimetableResponse>).exception)
                        is DataResponse.Success -> Change.TimetableLoaded((it.second as DataResponse.Success<TimetableResponse>).data)
                        is DataResponse.Loading -> Change.Loading(it.first)
                    }
                }

            val loadTimetableChange = actions
                .filterIsInstance<Action.LoadTimetable>()
                .flatMapLatest { repo.getTimetable(group, it.week) }
                .map {
                    when(it.second) {
                        is DataResponse.Error -> Change.Error(it.first, (it.second as DataResponse.Error<TimetableResponse>).exception)
                        is DataResponse.Success -> Change.TimetableLoaded((it.second as DataResponse.Success<TimetableResponse>).data)
                        is DataResponse.Loading -> Change.Loading(it.first)
                    }
                }

            val selectDateChange = actions
                .filterIsInstance<Action.SelectDate>()
                .map { Change.SelectDate(it.date) }

            val initChange = actions
                .filterIsInstance<Action.Init>()
                .flatMapLatest { repo.getTimetable(group) }
                .map {
                    when(it) {
                        is DataResponse.Error -> Change.Error(1, it.exception)
                        is DataResponse.Success -> Change.TimetableLoaded(it.data)
                        is DataResponse.Loading -> Change.Loading(1)
                    }
                }

            val allChanges = merge(updateTimetableChange, loadTimetableChange, selectDateChange, initChange)

            allChanges
                .scan(initialState, reducer)
                .distinctUntilChanged()
                .collect {
                    state.value = it
                    Log.d(TAG, "State updated: $it")
                }
        }
    }

    companion object {
        private const val TAG = "TimetableViewModel"
    }

}
