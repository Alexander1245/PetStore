package com.dart69.petstore.details.domain.usecases

import com.dart69.petstore.home.data.PetsRepository
import com.dart69.petstore.home.model.SelectablePet
import com.dart69.petstore.shared.model.Task
import kotlinx.coroutines.flow.Flow

interface FindPetByIdUseCase {
    operator fun invoke(id: Long): Flow<Task<SelectablePet>>

    class Implementation(
        private val repository: PetsRepository
    ) : FindPetByIdUseCase {
        override fun invoke(id: Long): Flow<Task<SelectablePet>> = repository.findByPrimaryKey(id)
    }
}