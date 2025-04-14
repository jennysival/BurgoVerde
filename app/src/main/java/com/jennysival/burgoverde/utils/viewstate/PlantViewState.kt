package com.jennysival.burgoverde.utils.viewstate

sealed class PlantViewState<out T> {
    data class Success<T>(val data: T) : PlantViewState<T>()
    data class Error(val throwable: Throwable) : PlantViewState<Nothing>()
    object None : PlantViewState<Nothing>()
}