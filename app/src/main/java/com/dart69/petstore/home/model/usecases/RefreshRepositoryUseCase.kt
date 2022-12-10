package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.data.PetsRepository

interface RefreshRepositoryUseCase {
    suspend operator fun invoke()

    class Implementation(
        private val repository: PetsRepository
    ) : RefreshRepositoryUseCase {
        override suspend fun invoke() {
            repository.refresh()
        }
    }
}