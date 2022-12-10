package com.dart69.petstore.details.domain.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.SelectablePet
import com.dart69.petstore.shared.data.repository.update

interface UpdatePetUseCase {
    suspend operator fun invoke(item: SelectablePet)

    class Implementation(
        private val petsRepository: PetsRepository
    ) : UpdatePetUseCase {
        override suspend fun invoke(item: SelectablePet) {
            petsRepository.update(item)
        }
    }
}