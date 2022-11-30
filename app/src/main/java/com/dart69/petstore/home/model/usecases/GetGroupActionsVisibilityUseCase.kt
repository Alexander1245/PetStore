package com.dart69.petstore.home.model.usecases

import android.view.View
import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.PetsSelectionTracker
import com.dart69.petstore.shared.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface GetGroupActionsVisibilityUseCase {
    operator fun invoke(): Flow<Int>

    class Implementation(
        private val repository: PetsRepository,
        private val tracker: PetsSelectionTracker
    ) : GetGroupActionsVisibilityUseCase {
        override fun invoke(): Flow<Int> =
            repository.observe().combine(tracker.observe()) { task, keys ->
                if (task is Task.Completed && keys.isNotEmpty()) View.VISIBLE else View.GONE
            }
    }
}