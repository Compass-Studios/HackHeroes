package io.github.compassstudios.hackheroesandroid.helpers

sealed class LoadingState<out T> {
    data object Loading : LoadingState<Nothing>()
    data class Success<T>(val data: T) : LoadingState<T>()
    data class Error<out E: Throwable>(val error: E) : LoadingState<Nothing>()
}
