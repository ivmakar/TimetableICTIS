package ru.ivmak.core.utils

sealed class DataResponse<T> {
    data class Success<T>(val data: T): DataResponse<T>()
    data class Error<T>(val exception: Exception? = null): DataResponse<T>()
    data class Loading<T>(val progress: Int = 0) : DataResponse<T>()
}
