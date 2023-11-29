package ru.ivmak.core.mvi

typealias Reducer<S, C> = (state: S, change: C) -> S
