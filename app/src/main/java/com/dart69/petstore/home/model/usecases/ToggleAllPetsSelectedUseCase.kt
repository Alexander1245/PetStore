package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.shared.model.selectMany
import com.dart69.petstore.shared.model.takeResult
import com.dart69.petstore.shared.model.unselectMany

interface ToggleAllPetsSelectedUseCase {
    operator fun invoke()

    class Implementation(
        private val repository: PetsRepository,
        private val tracker: PetsSelectionTracker
    ) : ToggleAllPetsSelectedUseCase {
        override operator fun invoke() {
            val items = repository.observe().value.takeResult() ?: return
            val action =
                if (items.size == tracker.count()) tracker::unselectMany else tracker::selectMany
            action(items)
        }
    }
}