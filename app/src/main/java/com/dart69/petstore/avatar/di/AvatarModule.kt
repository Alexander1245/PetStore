package com.dart69.petstore.avatar.di

import com.dart69.petstore.avatar.domain.usecases.LoadNextUseCase
import com.dart69.petstore.avatar.domain.usecases.LoadPreviousUseCase
import com.dart69.petstore.home.data.PetsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object AvatarModule {

    @Provides
    fun provideLoadNextUseCase(repository: PetsRepository): LoadNextUseCase =
        LoadNextUseCase.Implementation(repository)

    @Provides
    fun provideLoadPreviousUseCase(repository: PetsRepository): LoadPreviousUseCase =
        LoadPreviousUseCase.Implementation(repository)
}