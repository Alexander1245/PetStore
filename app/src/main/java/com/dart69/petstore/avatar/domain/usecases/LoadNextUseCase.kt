package com.dart69.petstore.avatar.domain.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.domain.model.SelectablePet
import com.dart69.petstore.shared.itemAfter

interface LoadNextUseCase {
    suspend operator fun invoke(current: SelectablePet): SelectablePet?

    class Implementation(
        private val repository: PetsRepository,
    ) : LoadNextUseCase {
        override suspend operator fun invoke(current: SelectablePet): SelectablePet? =
            repository.retrieve().itemAfter { it.id == current.id }
    }
}