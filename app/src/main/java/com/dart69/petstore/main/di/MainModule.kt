package com.dart69.petstore.main.di

import com.dart69.petstore.main.domain.usecases.ObserveMessagesUseCase
import com.dart69.petstore.shared.model.MessageObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object MainModule {

    @Provides
    fun provideObserveMessagesUseCase(messageObserver: MessageObserver): ObserveMessagesUseCase =
        ObserveMessagesUseCase.Implementation(messageObserver)
}