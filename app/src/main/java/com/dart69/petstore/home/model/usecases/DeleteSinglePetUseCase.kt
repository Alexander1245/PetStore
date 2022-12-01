package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.home.presentation.SelectablePet
import com.dart69.petstore.shared.data.repository.delete
import com.dart69.petstore.shared.model.unselect

interface DeleteSinglePetUseCase {
    suspend operator fun invoke(item: SelectablePet)

    class Implementation(
        private val repository: PetsRepository,
        private val tracker: PetsSelectionTracker
    ) : DeleteSinglePetUseCase {
        override suspend fun invoke(item: SelectablePet) {
            val pet = item.source
            tracker.unselect(pet)
            repository.delete(pet)
        }
    }
}