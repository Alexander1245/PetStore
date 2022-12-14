package com.dart69.petstore.shared.model

import kotlinx.coroutines.CoroutineDispatcher

interface AvailableDispatchers {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
}