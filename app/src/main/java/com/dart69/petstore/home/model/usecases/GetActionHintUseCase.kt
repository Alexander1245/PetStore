package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.R
import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.shared.model.takeResult
import com.dart69.petstore.shared.presentation.ResourceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface GetActionHintUseCase {
    operator fun invoke(): Flow<String>

    class Implementation(
        private val repository: PetsRepository,
        private val tracker: PetsSelectionTracker,
        private val resourceManager: ResourceManager
    ) : GetActionHintUseCase {
        override fun invoke(): Flow<String> =
            repository.observe().combine(tracker.observe()) { task, keys ->
                val items = task.takeResult() ?: emptyList()
                val stringRes =
                    if (items.count() == keys.count()) R.string.unselect_all else R.string.select_all
                resourceManager.getString(stringRes)
            }
    }
}