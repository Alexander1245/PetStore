package com.dart69.petstore.presentation

import com.dart69.petstore.shared.model.AvailableDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatchers : AvailableDispatchers {
    private val testDispatcher = StandardTestDispatcher()

    override val io: CoroutineDispatcher
        get() = testDispatcher
    override val main: CoroutineDispatcher
        get() = testDispatcher
    override val default: CoroutineDispatcher
        get() = testDispatcher
}