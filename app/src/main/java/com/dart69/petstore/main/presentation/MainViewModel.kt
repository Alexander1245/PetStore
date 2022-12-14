package com.dart69.petstore.main.presentation

import androidx.lifecycle.ViewModel
import com.dart69.petstore.main.domain.usecases.ObserveMessagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    observeMessagesUseCase: ObserveMessagesUseCase
) : ViewModel() {
    val messages = observeMessagesUseCase()
}