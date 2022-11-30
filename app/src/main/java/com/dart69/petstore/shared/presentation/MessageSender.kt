package com.dart69.petstore.shared.presentation

import androidx.annotation.StringRes

interface MessageSender {
    fun sendMessage(message: String)

    fun sendMessage(@StringRes messageRes: Int)
}