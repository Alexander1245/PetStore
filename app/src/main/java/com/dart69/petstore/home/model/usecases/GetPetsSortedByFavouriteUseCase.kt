package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.home.presentation.SelectablePet
import com.dart69.petstore.shared.model.Task
import com.dart69.petstore.shared.model.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface GetPetsSortedByFavouriteUseCase {
    operator fun invoke(): Flow<Task<List<SelectablePet>>>

    class Implementation(
        private val repository: PetsRepository,
        private val tracker: PetsSelectionTracker
    ) : GetPetsSortedByFavouriteUseCase {
        override operator fun invoke(): Flow<Task<List<SelectablePet>>> =
            repository.observe().combine(tracker.observe()) { task, keys ->
                task.map { items ->
                    items
                        .map { pet -> SelectablePet(pet, pet.id in keys) }
                        .sortedByDescending { item -> item.isFavourite }
                }
            }
    }
}

