package com.dart69.petstore.details.domain.usecases

import com.dart69.petstore.shared.presentation.ImageLoader

interface DownloadAvatarUseCase {
    operator fun invoke(imageUri: String)

    class Implementation(
        private val imageLoader: ImageLoader
    ): DownloadAvatarUseCase {
        override fun invoke(imageUri: String) {
            imageLoader.download(imageUri)
        }
    }
}