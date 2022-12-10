package com.dart69.petstore.main.presentation

import androidx.lifecycle.ViewModel
import com.dart69.petstore.main.domain.usecases.ObserveMessagesUseCase

class MainViewModel(
    observeMessagesUseCase: ObserveMessagesUseCase
) : ViewModel() {
    val messages = observeMessagesUseCase()
}