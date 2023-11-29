package ru.ivmak.core.mvi

interface BaseState {
    fun obfuscatedString() = "${javaClass.simpleName}@${hashCode()}"
}
