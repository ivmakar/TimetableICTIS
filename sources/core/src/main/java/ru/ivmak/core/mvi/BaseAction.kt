package ru.ivmak.core.mvi

interface BaseAction {
    fun obfuscatedString() = "${javaClass.simpleName}@${hashCode()}"
}
