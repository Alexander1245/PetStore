package com.dart69.petstore.shared.model

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

interface Logger {
    fun <T> logEachEmit(tag: String, initial: T, flow: Flow<T>)

    object None : Logger {
        override fun <T> logEachEmit(tag: String, initial: T, flow: Flow<T>) {}
    }

    class Default(
        private val coroutineScope: CoroutineScope
    ) : Logger {
        override fun <T> logEachEmit(tag: String, initial: T, flow: Flow<T>) {
            flow.onEach {
                val message = if(it is Collection<*>) it.joinToString() else it.toString()
                Log.d(tag, message)
            }.stateIn(coroutineScope, SharingStarted.Eagerly, initial)
        }
    }
}