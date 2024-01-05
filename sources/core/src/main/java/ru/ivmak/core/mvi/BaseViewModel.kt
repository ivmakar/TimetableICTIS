package ru.ivmak.core.mvi

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BaseViewModel<A : BaseAction, S : BaseState> {

    val actions: SharedFlow<A>

    val observableState: StateFlow<S>

    fun dispatch(action: A)
}
