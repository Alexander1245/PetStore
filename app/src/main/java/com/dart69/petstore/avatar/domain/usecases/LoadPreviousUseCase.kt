package com.dart69.petstore.avatar.domain.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.domain.model.SelectablePet
import com.dart69.petstore.shared.itemBefore

interface LoadPreviousUseCase {
    suspend operator fun invoke(current: SelectablePet): SelectablePet?

    class Implementation(
        private val repository: PetsRepository,
    ) : LoadPreviousUseCase {
        override suspend fun invoke(current: SelectablePet): SelectablePet? =
            repository.retrieve().itemBefore { current.id == it.id }
    }
}