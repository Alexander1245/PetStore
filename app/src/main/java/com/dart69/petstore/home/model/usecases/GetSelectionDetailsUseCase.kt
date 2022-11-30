package com.dart69.petstore.home.model.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.shared.model.SelectionDetails
import com.dart69.petstore.shared.model.takeResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface GetSelectionDetailsUseCase {
    operator fun invoke(): Flow<SelectionDetails>

    class Implementation(
        private val repository: PetsRepository,
        private val tracker: PetsSelectionTracker
    ) : GetSelectionDetailsUseCase {
        override fun invoke(): Flow<SelectionDetails> =
            repository.observe().combine(tracker.observe()) { task, keys ->
                val items = task.takeResult() ?: emptyList()
                SelectionDetails(selected = keys.count(), total = items.count())
            }
    }
}