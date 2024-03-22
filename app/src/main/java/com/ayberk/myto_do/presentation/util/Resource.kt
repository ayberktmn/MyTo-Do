package com.ayberk.myto_do.presentation.util

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Fail(val message: String) : Resource<Nothing>()
    data class Error(val throwable: String) : Resource<Nothing>()
    class Unspecified<T> : Resource<T>()
}
