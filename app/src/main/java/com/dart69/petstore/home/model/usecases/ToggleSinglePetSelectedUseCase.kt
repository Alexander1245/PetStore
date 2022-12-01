package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.home.presentation.SelectablePet
import com.dart69.petstore.shared.model.toggle

interface ToggleSinglePetSelectedUseCase {
    operator fun invoke(item: SelectablePet)

    class Implementation(
        private val selectionTracker: PetsSelectionTracker
    ) : ToggleSinglePetSelectedUseCase {
        override fun invoke(item: SelectablePet) {
            selectionTracker.toggle(item.source)
        }
    }
}