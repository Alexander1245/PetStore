package com.dart69.petstore.details.di

import com.dart69.petstore.details.domain.usecases.DownloadAvatarUseCase
import com.dart69.petstore.details.domain.usecases.SendMessageUseCase
import com.dart69.petstore.details.domain.usecases.UpdatePetUseCase
import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.shared.model.MessageObserver
import com.dart69.petstore.shared.presentation.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DetailsModule {

    @Provides
    fun provideDownloadAvatarUseCase(imageLoader: ImageLoader): DownloadAvatarUseCase =
        DownloadAvatarUseCase.Implementation(imageLoader)

    @Provides
    fun provideSendMessageUseCase(messageObserver: MessageObserver): SendMessageUseCase =
        SendMessageUseCase.Implementation(messageObserver)

    @Provides
    fun provideUpdatePetUseCase(repository: PetsRepository): UpdatePetUseCase =
        UpdatePetUseCase.Implementation(repository)
}