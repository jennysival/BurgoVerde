package com.jennysival.burgoverde.utils.viewstate

sealed class ProfileViewState<out T> {
    data class Success<T>(val data: T) : ProfileViewState<T>()
    data class Error(val throwable: Throwable) : ProfileViewState<Nothing>()
}