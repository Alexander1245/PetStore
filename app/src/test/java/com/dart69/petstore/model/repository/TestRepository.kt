package com.dart69.petstore.model.repository

import com.dart69.petstore.data.repository.FavouriteItemRepository
import com.dart69.petstore.model.item.Pet
import kotlinx.coroutines.delay

class TestRepository(dataSize: Int) : FavouriteItemRepository<Long, Pet> {
    private var listener: ((List<Pet>) -> Unit)? = null
    private var data = List(dataSize) {
        Pet(it.toLong(), "Name $it", "Details $it")
    }

    override suspend fun getAllSortedByFavourite(): List<Pet> =
        data.sortedByDescending { it.isFavourite }

    override suspend fun deleteByKeys(keys: List<Long>): Int {
        val oldSize = data.size
        val items = keys.mapNotNull { key -> data.find { it.id == key } }
        data = (data - items.toSet()).toMutableList()
        listener?.invoke(getAllSortedByFavourite())
        return oldSize - data.size
    }

    override suspend fun findByKeys(keys: List<Long>): List<Pet> {
        val unique = keys.toSet()
        return data.filter { it.id in unique }
    }

    override fun addOnChangedListener(listener: (List<Pet>) -> Unit) {
        this.listener = listener
    }

    override fun removeOnChangedListener() {
        this.listener = null
    }

    override suspend fun getAll(): List<Pet> = data

    override suspend fun findByPrimaryKey(key: Long): Pet =
        data.find { it.id == key } ?: error("Can't find actual key in repository")

    override suspend fun insert(item: Pet): Long {
        data = (data + item).toMutableList()
        listener?.invoke(getAllSortedByFavourite())
        return item.id
    }

    override suspend fun insertMany(items: List<Pet>): List<Long> {
        val ids = items.map { it.id }
        data = (data + items).toMutableList()
        listener?.invoke(getAllSortedByFavourite())
        return ids
    }

    override suspend fun update(item: Pet): Int {
        val index = data.indexOfFirst { pet -> pet.id == item.id }
        if (index == -1) {
            error("Can't update non existing item")
        }
        data = data.toMutableList().apply { this[index] = item }
        listener?.invoke(getAllSortedByFavourite())
        return 1
    }

    override suspend fun updateMany(items: List<Pet>): Int {
        delay(555)
        val unique = items.toSet()
        unique.forEach { update(it) }
        return unique.size
    }

    override suspend fun delete(item: Pet): Int {
        data = (data - item).toMutableList()
        listener?.invoke(getAllSortedByFavourite())
        return 1
    }

    override suspend fun deleteMany(items: List<Pet>): Int {
        val oldSize = data.size
        data = (data - items.toSet()).toMutableList()
        listener?.invoke(getAllSortedByFavourite())
        return oldSize - data.size
    }

}