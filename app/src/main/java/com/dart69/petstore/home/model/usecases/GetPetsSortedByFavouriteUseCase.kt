package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.home.model.SelectablePet
import com.dart69.petstore.home.model.selectIf
import com.dart69.petstore.shared.model.AvailableDispatchers
import com.dart69.petstore.shared.model.Task
import com.dart69.petstore.shared.model.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn

interface GetPetsSortedByFavouriteUseCase {
    operator fun invoke(): Flow<Task<List<SelectablePet>>>

    class Implementation(
        repository: PetsRepository,
        tracker: PetsSelectionTracker,
        private val dispatchers: AvailableDispatchers
    ) : GetPetsSortedByFavouriteUseCase {
        private val pets = repository.observe()
        private val selectedKeys = tracker.observe()

        override operator fun invoke(): Flow<Task<List<SelectablePet>>> =
            pets.combine(selectedKeys) { task, keys ->
                task.map { items ->
                    items
                        .map { pet ->
                            pet.selectIf { it.id in keys }
                        }
                        .sortedByDescending { item -> item.isFavourite }
                }
            }.flowOn(dispatchers.default)
    }
}

