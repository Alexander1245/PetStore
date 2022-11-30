package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.Pet
import com.dart69.petstore.home.presentation.PetItem

interface ToggleSinglePetFavourite {
    suspend operator fun invoke(item: PetItem)

    class Implementation(
        private val repository: PetsRepository
    ) : ToggleSinglePetFavourite {
        override suspend fun invoke(item: PetItem) {
            val newItem = item.asSource<Pet>().toggleFavourite() as Pet
            repository.updateItems(newItem)
        }
    }
}