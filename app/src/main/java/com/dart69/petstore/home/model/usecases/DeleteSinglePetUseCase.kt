package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.Pet
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.home.presentation.PetItem
import com.dart69.petstore.shared.data.repository.ItemRepository
import com.dart69.petstore.shared.model.SelectionTracker

interface DeleteSinglePetUseCase {
    suspend operator fun invoke(item: PetItem)

    class Implementation(
        private val repository: PetsRepository,
        private val tracker: PetsSelectionTracker
    ): DeleteSinglePetUseCase {
        override suspend fun invoke(item: PetItem) {
            val pet = item.asSource<Pet>()
            tracker.unselect(pet)
            repository.deleteItems(pet)
        }
    }
}