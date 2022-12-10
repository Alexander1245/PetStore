package com.dart69.petstore.details.domain.usecases

import com.dart69.petstore.shared.model.MessageObserver

interface SendMessageUseCase {
    operator fun invoke(message: String)

    class Implementation(
        private val messageObserver: MessageObserver
    ) : SendMessageUseCase {
        override fun invoke(message: String) {
            messageObserver.send(message)
        }
    }
}