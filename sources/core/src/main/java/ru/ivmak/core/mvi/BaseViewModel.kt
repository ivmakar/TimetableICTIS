package ru.ivmak.core.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<A : BaseAction, S : BaseState> : ViewModel() {
    protected val TAG: String by lazy { javaClass.simpleName }

    private val _actions = MutableSharedFlow<A>()
    protected val actions: SharedFlow<A> = _actions.asSharedFlow()

    protected abstract val initialState: S

    protected val state = MutableStateFlow(initialState)

    val observableState: StateFlow<S> = state.asStateFlow()

    fun dispatch(action: A) {
        viewModelScope.launch {
            Log.d(TAG, "Received action: $action")
            _actions.emit(action)
        }
    }
}
