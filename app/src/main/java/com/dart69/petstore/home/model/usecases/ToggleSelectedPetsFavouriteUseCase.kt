package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.home.model.SelectablePet
import com.dart69.petstore.shared.model.takeResult

interface ToggleSelectedPetsFavouriteUseCase {
    suspend operator fun invoke()

    class Implementation(
        private val repository: PetsRepository,
        private val tracker: PetsSelectionTracker
    ) : ToggleSelectedPetsFavouriteUseCase {
        override suspend fun invoke() {
            val items = repository.observe().value.takeResult() ?: return
            val keys = tracker.observe().value
            val selected = items.filter { pet -> pet.id in keys }
            val action =
                if (selected.all { it.isFavourite }) SelectablePet::unmakeFavourite else SelectablePet::makeFavourite
            repository.update(selected.map { action(it) as SelectablePet })
        }
    }
}