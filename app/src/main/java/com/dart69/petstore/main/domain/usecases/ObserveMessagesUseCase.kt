package com.dart69.petstore.main.domain.usecases

import com.dart69.petstore.shared.model.MessageObserver
import kotlinx.coroutines.flow.SharedFlow

interface ObserveMessagesUseCase {
    operator fun invoke(): SharedFlow<String>

    class Implementation(
        private val messageObserver: MessageObserver
    ) : ObserveMessagesUseCase {
        override fun invoke(): SharedFlow<String> = messageObserver.observe()
    }
}