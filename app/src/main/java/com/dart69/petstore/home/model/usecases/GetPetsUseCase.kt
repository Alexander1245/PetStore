package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.home.presentation.PetItem
import com.dart69.petstore.shared.model.Task
import com.dart69.petstore.shared.model.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface GetPetsUseCase {
    operator fun invoke(): Flow<Task<List<PetItem>>>

    class Implementation(
        private val repository: PetsRepository,
        private val tracker: PetsSelectionTracker
    ) : GetPetsUseCase {
        override operator fun invoke(): Flow<Task<List<PetItem>>> =
            repository.observe().combine(tracker.observe()) { task, keys ->
                task.map { items ->
                    items.map { pet -> PetItem(pet, pet.id in keys) }
                }
            }
    }
}

