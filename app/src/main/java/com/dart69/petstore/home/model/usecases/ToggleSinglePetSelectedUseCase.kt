package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.model.Pet
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.home.presentation.PetItem
import com.dart69.petstore.shared.model.toggle

interface ToggleSinglePetSelectedUseCase {
    operator fun invoke(item: PetItem)

    class Implementation(
        private val selectionTracker: PetsSelectionTracker
    ) : ToggleSinglePetSelectedUseCase {
        override fun invoke(item: PetItem) {
            val pet = item.asSource<Pet>()
            selectionTracker.toggle(pet)
        }
    }
}