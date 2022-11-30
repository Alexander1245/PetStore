package com.dart69.petstore.home.data

import com.dart69.petstore.shared.data.repository.ItemRepository
import com.dart69.petstore.home.model.Pet

interface PetsRepository : ItemRepository<Long, Pet>

fun ItemRepository<Long, Pet>.asPetsRepository(): PetsRepository =
    object : PetsRepository, ItemRepository<Long, Pet> by this {}