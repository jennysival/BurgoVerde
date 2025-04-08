package com.jennysival.burgoverde.utils.viewstate

import com.jennysival.burgoverde.utils.mapper.AuthError

sealed class AuthViewState<out T> {
    data class Success<T>(val data: T) : AuthViewState<T>()
    data class Error(val authError: AuthError) : AuthViewState<Nothing>()
}
