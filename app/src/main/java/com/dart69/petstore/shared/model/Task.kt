package com.dart69.petstore.shared.model

import kotlinx.coroutines.CancellationException

sealed class Task<T> {
    class Completed<T>(val value: T) : Task<T>()

    class Error<T>(val throwable: Throwable) : Task<T>()

    class Loading<T> : Task<T>()

    companion object {
        fun <T> initial(value: T): Task<T> = Completed(value)
    }
}

fun <T, R> Task<T>.map(mapper: (T) -> R): Task<R> = when (this) {
    is Task.Error -> Task.Error(throwable)
    is Task.Loading -> Task.Loading()
    is Task.Completed -> Task.Completed(mapper(value))
}

fun <T> Task<T>.takeResult(): T? = if (this is Task.Completed) value else null

fun <T> Task<T>.onComplete(block: (T) -> Unit): Task<T> = apply {
    if (this is Task.Completed) block(value)
}

fun <T> Task<T>.onError(block: (Throwable) -> Unit): Task<T> = apply {
    if (this is Task.Error) block(throwable)
}

fun <T> Task<T>.onLoading(block: () -> Unit): Task<T> = apply {
    if (this is Task.Loading) block()
}

fun <T> Task<T>.onCancel(block: () -> Unit): Task<T> = apply {
    if (this is Task.Error && throwable is CancellationException) block()
}

fun Task<*>.isNotCompleted(): Boolean = this !is Task.Completed

fun Task<*>.isCompleted(): Boolean = this is Task.Completed