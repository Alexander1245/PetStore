package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.Pet
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.shared.data.repository.updateMany
import com.dart69.petstore.shared.model.takeResult

interface ToggleSelectedItemsToFavouriteUseCase {
    suspend operator fun invoke()

    class Implementation(
        private val repository: PetsRepository,
        private val tracker: PetsSelectionTracker
    ) : ToggleSelectedItemsToFavouriteUseCase {
        override suspend fun invoke() {
            val items = repository.observe().value.takeResult() ?: return
            val keys = tracker.observe().value
            val selected = items.filter { pet -> pet.id in keys }
            val action =
                if (selected.all { it.isFavourite }) Pet::unmakeFavourite else Pet::makeFavourite
            repository.updateMany(selected.map { action(it) })
        }
    }
}