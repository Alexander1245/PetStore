package com.dart69.petstore.presentation.utils

import androidx.annotation.StringRes

interface MessageSender {
    fun sendMessage(text: String)

    fun sendMessage(@StringRes textRes: Int)
}