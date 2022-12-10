package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.home.model.SelectablePet
import com.dart69.petstore.shared.model.toggle

interface TogglePetSelectedUseCase {
    operator fun invoke(item: SelectablePet)

    class Implementation(
        private val selectionTracker: PetsSelectionTracker
    ) : TogglePetSelectedUseCase {
        override fun invoke(item: SelectablePet) {
            selectionTracker.toggle(item)
        }
    }
}