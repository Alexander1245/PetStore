package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.shared.model.takeResult

interface DeleteSelectedPetsUseCase {
    suspend operator fun invoke()

    class Implementation(
        private val repository: PetsRepository,
        private val tracker: PetsSelectionTracker
    ) : DeleteSelectedPetsUseCase {
        override suspend fun invoke() {
            val items = repository.observe().value.takeResult() ?: return
            val keys = tracker.observe().value
            val selected = items.filter { pet -> pet.id in keys }
            tracker.unselect(selected)
            repository.delete(selected)
        }
    }
}