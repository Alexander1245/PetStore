package com.dart69.petstore.home.data

import com.dart69.petstore.home.domain.model.SelectablePet
import com.dart69.petstore.shared.data.repository.ItemRepository

interface PetsRepository : ItemRepository<Long, SelectablePet>

fun ItemRepository<Long, SelectablePet>.asPetsRepository(): PetsRepository =
    object : PetsRepository, ItemRepository<Long, SelectablePet> by this {}