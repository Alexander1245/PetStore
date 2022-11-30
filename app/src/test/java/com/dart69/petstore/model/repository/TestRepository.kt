package com.dart69.petstore.model.repository

import com.dart69.petstore.shared.model.AvailableDispatchers
import com.dart69.petstore.home.model.Pet
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class TestRepository(
    dataSize: Int,
    private val dispatchers: AvailableDispatchers
) : FavouriteItemRepository<Long, Pet> {
    private var listener: ((List<Pet>) -> Unit)? = null
    private var data = List(dataSize) {
        Pet(it.toLong(), "Name $it", "Details $it")
    }

    override suspend fun getAllSortedByFavourite(): List<Pet> =
        data.sortedByDescending { it.isFavourite }.also { listener?.invoke(it) }

    override suspend fun deleteByKeys(keys: List<Long>): Int = withContext(dispatchers.default) {
        val oldSize = data.size
        val items = keys.mapNotNull { key -> data.find { it.id == key } }
        data = (data - items.toSet()).toMutableList()
        listener?.invoke(getAllSortedByFavourite())
        oldSize - data.size
    }

    override suspend fun findByKeys(keys: List<Long>): List<Pet> =
        withContext(dispatchers.default) {
            val unique = keys.toSet()
            data.filter { it.id in unique }
        }

    override fun addOnChangedListener(listener: (List<Pet>) -> Unit) {
        this.listener = listener
    }

    override fun removeOnChangedListener() {
        this.listener = null
    }

    override suspend fun getAll(): List<Pet> = data

    override suspend fun findByPrimaryKey(key: Long): Pet = withContext(dispatchers.default) {
        data.find { it.id == key } ?: error("Can't find actual key in repository")
    }

    override suspend fun insert(item: Pet): Long = withContext(dispatchers.main) {
        data = (data + item).toMutableList()
        listener?.invoke(getAllSortedByFavourite())
        item.id
    }

    override suspend fun insertMany(items: List<Pet>): List<Long> =
        withContext(dispatchers.default) {
            val ids = items.map { it.id }
            data = (data + items).toMutableList()
            listener?.invoke(getAllSortedByFavourite())
            ids
        }

    override suspend fun update(item: Pet): Int = withContext(dispatchers.default) {
        val index = data.indexOfFirst { pet -> pet.id == item.id }
        if (index == -1) {
            error("Can't update non existing item")
        }
        data = data.toMutableList().apply { this[index] = item }
        listener?.invoke(getAllSortedByFavourite())
        1
    }

    override suspend fun updateMany(items: List<Pet>): Int = withContext(dispatchers.default) {
        delay(555)
        val unique = items.toSet()
        unique.forEach { update(it) }
        unique.size
    }

    override suspend fun delete(item: Pet): Int = withContext(dispatchers.default) {
        data = (data - item).toMutableList()
        listener?.invoke(getAllSortedByFavourite())
        1
    }

    override suspend fun deleteMany(items: List<Pet>): Int = withContext(dispatchers.default) {
        val oldSize = data.size
        data = (data - items.toSet()).toMutableList()
        listener?.invoke(getAllSortedByFavourite())
        oldSize - data.size
    }
}