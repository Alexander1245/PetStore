package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.shared.model.takeResult

interface ToggleAllPetsSelectedUseCase {
    operator fun invoke()

    class Implementation(
        private val repository: PetsRepository,
        private val tracker: PetsSelectionTracker
    ) : ToggleAllPetsSelectedUseCase {
        override operator fun invoke() {
            val items = repository.observe().value.takeResult() ?: return
            val action =
                if (items.size == tracker.count()) tracker::unselect else tracker::select
            action(items)
        }
    }
}