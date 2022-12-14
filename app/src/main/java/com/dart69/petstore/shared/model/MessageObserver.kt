package com.dart69.petstore.shared.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

interface MessageObserver {
    fun send(message: String)

    fun observe(): SharedFlow<String>

    class Implementation(
        private val applicationScope: CoroutineScope,
        private val dispatchers: AvailableDispatchers
    ) : MessageObserver {
        private val messages = MutableSharedFlow<String>()

        override fun send(message: String) {
            applicationScope.launch(dispatchers.default) {
                messages.emit(message)
            }
        }

        override fun observe(): SharedFlow<String> = messages.asSharedFlow()
    }
}