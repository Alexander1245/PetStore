package com.dart69.petstore.shared.model

sealed class Task<T> {
    class Completed<T>(val value: T) : Task<T>()

    class Error<T>(val message: String?) : Task<T>()

    class Loading<T> : Task<T>()

    companion object {
        fun <T> initial(value: T): Task<T> = Completed(value)
    }
}

fun <T, R> Task<T>.map(mapper: (T) -> R): Task<R> = when (this) {
    is Task.Error -> Task.Error(message)
    is Task.Loading -> Task.Loading()
    is Task.Completed -> Task.Completed(mapper(value))
}

fun <T> Task<T>.takeResult(): T? = if (this is Task.Completed) value else null

fun <T> Task<T>.onComplete(block: (T) -> Unit): Task<T> = apply {
    takeResult()?.let { result -> block(result) }
}