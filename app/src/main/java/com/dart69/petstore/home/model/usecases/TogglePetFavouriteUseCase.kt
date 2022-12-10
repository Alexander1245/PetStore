package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.SelectablePet
import com.dart69.petstore.shared.data.repository.update
import com.dart69.petstore.shared.model.item.toggleFavourite

interface TogglePetFavouriteUseCase {
    suspend operator fun invoke(item: SelectablePet)

    class Implementation(
        private val repository: PetsRepository
    ) : TogglePetFavouriteUseCase {
        override suspend fun invoke(item: SelectablePet) {
            val newItem = item.toggleFavourite()
            repository.update(newItem)
        }
    }
}