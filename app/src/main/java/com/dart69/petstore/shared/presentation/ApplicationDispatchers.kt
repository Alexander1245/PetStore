package com.dart69.petstore.shared.presentation

import com.dart69.petstore.shared.model.AvailableDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ApplicationDispatchers @Inject constructor() : AvailableDispatchers {
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
}