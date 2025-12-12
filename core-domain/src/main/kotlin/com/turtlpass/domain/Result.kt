package com.turtlpass.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * A generic class that holds a value with its loading state
 *
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()

    data class Error(val error: Throwable? = null) : Result<Nothing>()

    object Loading : Result<Nothing>()

    fun isLoading() = this is Loading
    fun isSuccessful() = this is Success
    fun isError() = this is Error

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$error]"
            Loading -> "Loading"
        }
    }
}

val <T> Result<T>.data: T?
    get() = (this as? Result.Success)?.data

fun <T> Result<T>?.successOrFallback(fallback: T): T {
    if (this == null) return fallback
    return (this as? Result.Success<T>)?.data ?: fallback
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}

internal inline fun <T> Result<T>.switch(
    success : (T?) -> Unit = {},
    error : (throwable: Throwable?) -> Unit = {},
    loading: () -> Unit = {}
){
    when(this){
        is Result.Success -> success(this.data)
        is Result.Error -> error(this.error)
        is Result.Loading -> loading()
    }
}
