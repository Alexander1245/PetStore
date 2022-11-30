package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.Pet
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.shared.model.Task
import com.dart69.petstore.shared.model.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface GetSelectedPetsUseCase {
    operator fun invoke(): Flow<Task<List<Pet>>>

    class Implementation(
        private val repository: PetsRepository,
        private val tracker: PetsSelectionTracker
    ) : GetSelectedPetsUseCase {
        override fun invoke(): Flow<Task<List<Pet>>> =
            repository.observe().combine(tracker.observe()) { task, keys ->
                task.map { pets ->
                    pets.filter { it.id in keys }
                }
            }
    }
}